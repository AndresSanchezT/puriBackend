package com.AndresSanchezDev.SISTEMASPURI.Auth;


import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.LoginRequest;
import com.AndresSanchezDev.SISTEMASPURI.entity.DTO.LoginResponse;
import com.AndresSanchezDev.SISTEMASPURI.entity.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioSecurityService usuarioService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UsuarioSecurityService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsuario(), loginRequest.getPassword())
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String jwt = jwtService.generateToken(usuario);

        LoginResponse response = new LoginResponse(jwt, usuario.getUsername(),
                usuario.getRol(), usuario.getNombre());

        return ResponseEntity.ok(response);
    }
}