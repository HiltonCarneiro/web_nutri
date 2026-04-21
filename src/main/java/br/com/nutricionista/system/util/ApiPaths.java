package br.com.nutricionista.system.util;

public final class ApiPaths {

    public static final String API_V1 = "/api/v1";
    public static final String HEALTH = API_V1 + "/health";
    public static final String PACIENTES = API_V1 + "/pacientes";
    public static final String PACIENTE_EVOLUCOES = PACIENTES + "/{pacienteId}/evolucoes";
    public static final String PACIENTE_EVOLUCAO_POR_ID = PACIENTE_EVOLUCOES + "/{evolucaoId}";
    public static final String PACIENTE_PRONTUARIO = PACIENTES + "/{pacienteId}/prontuario";
    public static final String PACIENTE_AVALIACOES_ANTROPOMETRICAS = PACIENTES + "/{pacienteId}/avaliacoes-antropometricas";
    public static final String PACIENTE_AVALIACAO_ANTROPOMETRICA_POR_ID = PACIENTE_AVALIACOES_ANTROPOMETRICAS + "/{avaliacaoId}";
    public static final String PACIENTE_HISTORICO_ANTROPOMETRICO = PACIENTES + "/{pacienteId}/historico-antropometrico";
    public static final String PACIENTE_DIETAS = PACIENTES + "/{pacienteId}/dietas";
    public static final String PACIENTE_DIETA_POR_ID = PACIENTE_DIETAS + "/{dietaId}";
    public static final String PACIENTE_HISTORICO_DIETAS = PACIENTES + "/{pacienteId}/historico-dietas";
    public static final String PACIENTE_DIETA_REFEICOES = PACIENTE_DIETA_POR_ID + "/refeicoes";
    public static final String PACIENTE_DIETA_REFEICAO_POR_ID = PACIENTE_DIETA_REFEICOES + "/{refeicaoId}";
    public static final String PACIENTE_DIETA_REFEICAO_ITENS = PACIENTE_DIETA_REFEICAO_POR_ID + "/itens";
    public static final String PACIENTE_DIETA_REFEICAO_ITEM_POR_ID = PACIENTE_DIETA_REFEICAO_ITENS + "/{itemId}";
    public static final String PACIENTE_AGENDAMENTOS = PACIENTES + "/{pacienteId}/agendamentos";
    public static final String PACIENTE_AGENDAMENTO_POR_ID = PACIENTE_AGENDAMENTOS + "/{agendamentoId}";
    public static final String PACIENTE_AGENDAMENTO_STATUS = PACIENTE_AGENDAMENTO_POR_ID + "/status";
    public static final String PACIENTE_AGENDAMENTO_CANCELAMENTO = PACIENTE_AGENDAMENTO_POR_ID + "/cancelamento";
    public static final String AGENDAMENTOS = API_V1 + "/agendamentos";
    public static final String AGENDAMENTOS_DO_DIA = AGENDAMENTOS + "/hoje";
    public static final String AGENDAMENTOS_POR_PERIODO = AGENDAMENTOS + "/periodo";

    private ApiPaths() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada.");
    }
}