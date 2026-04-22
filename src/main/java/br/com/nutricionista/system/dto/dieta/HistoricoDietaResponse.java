package br.com.nutricionista.system.dto.dieta;

import java.time.LocalDateTime;

public record HistoricoDietaResponse(
        Long id,
        String titulo,
        String objetivoDieta,
        Boolean ativa,
        Integer quantidadeRefeicoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}