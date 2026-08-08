package br.com.atelieritalara.backend.dto.auth;

public record LoginRequest(
        String email,
        String senha
) {
}