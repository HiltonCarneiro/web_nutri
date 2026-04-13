package br.com.nutricionista.system.util;

public final class ApiPaths {

    public static final String API_V1 = "/api/v1";
    public static final String HEALTH = API_V1 + "/health";

    private ApiPaths() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }
}