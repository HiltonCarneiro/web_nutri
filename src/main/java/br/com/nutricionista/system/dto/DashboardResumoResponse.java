package br.com.nutricionista.system.dto;

public record DashboardResumoResponse(
        Long totalPacientes,
        Long totalAgendamentosHoje,
        Long totalAgendamentosPendentesHoje,
        Long totalDietasAtivas
) {
}