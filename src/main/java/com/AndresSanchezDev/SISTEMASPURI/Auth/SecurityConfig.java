package com.AndresSanchezDev.SISTEMASPURI.Auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de seguridad del sistema
 * <p>
 * ROLES:
 * - ADMINISTRADOR: Acceso total
 * - VENDEDOR: Crear/ver pedidos, visitas, consultar clientes y productos
 * - REPARTIDOR: Ver pedidos y cambiar estado de entrega
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UsuarioSecurityService usuarioService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UsuarioSecurityService usuarioService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.usuarioService = usuarioService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ========================================
                        // ENDPOINTS PÚBLICOS (Sin autenticación)
                        // ========================================
                        .requestMatchers("/api/auth/**").permitAll()

                        // ========================================
                        // RUTAS ESPECÍFICAS PRIMERO
                        // ========================================
                        // Boletas - Ruta específica /hoy PRIMERO
                        .requestMatchers(HttpMethod.GET, "/api/boletas/hoy")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")

                        // Boletas - Ruta específica /datos
                        .requestMatchers(HttpMethod.GET, "/api/boletas/*/datos")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")

                        // Pedidos - Rutas específicas de estado PRIMERO
                        .requestMatchers(HttpMethod.PATCH, "/api/pedidos/*/estado", "/api/pedidos/*/estado-movil")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")

                        // ========================================
                        // CLIENTES - Permisos por método HTTP
                        // ========================================
                        .requestMatchers(HttpMethod.GET, "/api/clientes/**")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")
                        .requestMatchers(HttpMethod.POST, "/api/clientes/**")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/clientes/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/**")
                        .hasRole("ADMINISTRADOR")

                        // ========================================
                        // PRODUCTOS - Permisos por método HTTP
                        // ========================================
                        .requestMatchers(HttpMethod.GET, "/api/productos/**")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")
                        .requestMatchers(HttpMethod.POST, "/api/productos/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**")
                        .hasRole("ADMINISTRADOR")

                        // ========================================
                        // PEDIDOS - Permisos por método HTTP
                        // ========================================
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/**")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")
                        .requestMatchers(HttpMethod.POST, "/api/pedidos/**")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/pedidos/**")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/pedidos/**")
                        .hasRole("ADMINISTRADOR")

                        // ========================================
                        // VISITAS - Permisos por método HTTP
                        // ========================================
                        .requestMatchers(HttpMethod.GET, "/api/visitas/**")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                        .requestMatchers(HttpMethod.POST, "/api/visitas/**")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/visitas/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/visitas/**")
                        .hasRole("ADMINISTRADOR")

                        // ========================================
                        // BOLETAS - Permisos por método HTTP
                        // ========================================
                        .requestMatchers(HttpMethod.GET, "/api/boletas/**")
                        .hasAnyRole("ADMINISTRADOR", "VENDEDOR", "REPARTIDOR")
                        .requestMatchers(HttpMethod.POST, "/api/boletas/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/boletas/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/boletas/**")
                        .hasRole("ADMINISTRADOR")

                        // ========================================
                        // RECURSOS SOLO ADMINISTRADOR
                        // ========================================
                        .requestMatchers(HttpMethod.GET, "/api/detallePedidos/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/detallePedidos/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/detallePedidos/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/detallePedidos/**")
                        .hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/vendedores/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/vendedores/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/vendedores/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/vendedores/**")
                        .hasRole("ADMINISTRADOR")

                        // ========================================
                        // DENEGAR TODO LO DEMÁS
                        // ========================================
                        .anyRequest().denyAll()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
                "GET",      // Consultar
                "POST",     // Crear
                "PUT",      // Actualizar completo
                "PATCH",    // Actualizar parcial (cambio de estado)
                "DELETE",   // Eliminar
                "OPTIONS"   // Preflight CORS
        ));

        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Headers expuestos al cliente
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        // Permitir credenciales (cookies, headers de autorización)
        configuration.setAllowCredentials(true);

        // Cache de preflight requests por 1 hora
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}