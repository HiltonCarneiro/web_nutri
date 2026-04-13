package br.com.nutricionista.system.security;

public final class SecurityConstants {

    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/health"
    };

    private SecurityConstants() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }
}