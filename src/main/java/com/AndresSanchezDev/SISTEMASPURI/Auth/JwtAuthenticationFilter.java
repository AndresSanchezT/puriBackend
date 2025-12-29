package com.AndresSanchezDev.SISTEMASPURI.Auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioSecurityService usuarioService;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioSecurityService usuarioService) {
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.usuarioService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                // ✅ CAMBIO IMPORTANTE: Extraer el rol del TOKEN, no de UserDetails
                String role = jwtService.getRoleFromToken(jwt);

                // 🔍 Debug logs (puedes comentarlos después)
                System.out.println("🔍 Username: " + username);
                System.out.println("🔍 Role from token: " + role);

                // ✅ Crear authorities desde el token
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                if (role != null && !role.isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }

                System.out.println("🔍 Authorities: " + authorities);

                // ✅ Usar las authorities del token, NO de userDetails
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities  // ← AQUÍ ESTÁ EL CAMBIO CLAVE
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
