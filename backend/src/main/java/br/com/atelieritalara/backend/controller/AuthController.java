package br.com.atelieritalara.backend.controller;

import br.com.atelieritalara.backend.dto.auth.LoginRequest;
import br.com.atelieritalara.backend.dto.auth.LoginResponse;
import br.com.atelieritalara.backend.entity.Usuario;
import br.com.atelieritalara.backend.repository.UsuarioRepository;
import br.com.atelieritalara.backend.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("E-mail ou senha inválidos")
                );

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new RuntimeException("E-mail ou senha inválidos");
        }

        String token = jwtService.gerarToken(
                usuario.getEmail(),
                usuario.getPerfil()
        );

        return new LoginResponse(token);
    }
}