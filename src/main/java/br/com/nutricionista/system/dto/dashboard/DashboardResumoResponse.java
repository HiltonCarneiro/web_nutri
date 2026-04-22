package br.com.nutricionista.system.dto.dashboard;

public record DashboardResumoResponse(
        Long totalPacientes,
        Long totalAgendamentosHoje,
        Long totalAgendamentosPendentesHoje,
        Long totalDietasAtivas
) {
}