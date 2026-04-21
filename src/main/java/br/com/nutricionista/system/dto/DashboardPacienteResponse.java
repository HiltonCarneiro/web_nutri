package br.com.nutricionista.system.dto;

import java.time.LocalDateTime;

public record DashboardPacienteResponse(
        Long id,
        String nome,
        LocalDateTime createdAt
) {
}