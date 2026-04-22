package br.com.nutricionista.system.dto.dashboard;

import java.time.LocalDateTime;

public record DashboardPacienteResponse(
        Long id,
        String nome,
        LocalDateTime createdAt
) {
}