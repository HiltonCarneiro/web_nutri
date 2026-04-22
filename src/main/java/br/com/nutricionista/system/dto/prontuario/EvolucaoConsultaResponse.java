package br.com.nutricionista.system.dto.prontuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EvolucaoConsultaResponse(
        Long id,
        LocalDate dataConsulta,
        String observacoes,
        String condutas,
        String queixasPaciente,
        String metasDefinidas,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}