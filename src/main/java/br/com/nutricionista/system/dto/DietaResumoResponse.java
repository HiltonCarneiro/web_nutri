package br.com.nutricionista.system.dto;

import java.time.LocalDateTime;

public record DietaResumoResponse(
        Long id,
        String titulo,
        String objetivoDieta,
        Boolean ativa,
        Integer quantidadeRefeicoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}