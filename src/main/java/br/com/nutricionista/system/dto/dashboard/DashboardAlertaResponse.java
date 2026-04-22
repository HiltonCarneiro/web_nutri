package br.com.nutricionista.system.dto.dashboard;

public record DashboardAlertaResponse(
        String tipo,
        Long quantidade
) {
}