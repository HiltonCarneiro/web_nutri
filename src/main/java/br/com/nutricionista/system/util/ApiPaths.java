package br.com.nutricionista.system.util;

public final class ApiPaths {

    public static final String API_V1 = "/api/v1";
    public static final String HEALTH = API_V1 + "/health";
    public static final String PACIENTES = API_V1 + "/pacientes";
    public static final String PACIENTE_EVOLUCOES = PACIENTES + "/{pacienteId}/evolucoes";
    public static final String PACIENTE_EVOLUCAO_POR_ID = PACIENTE_EVOLUCOES + "/{evolucaoId}";
    public static final String PACIENTE_PRONTUARIO = PACIENTES + "/{pacienteId}/prontuario";

    private ApiPaths() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }
}