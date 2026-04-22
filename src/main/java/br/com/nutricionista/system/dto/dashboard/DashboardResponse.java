package br.com.nutricionista.system.dto.dashboard;

import java.util.List;

public record DashboardResponse(
        DashboardResumoResponse resumo,
        List<DashboardAgendamentoResponse> proximosAgendamentos,
        List<DashboardPacienteResponse> pacientesRecentes,
        List<DashboardAlertaResponse> alertas
) {
}