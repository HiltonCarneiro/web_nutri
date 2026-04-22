package br.com.nutricionista.system.dto.dieta;

import java.time.LocalDateTime;

public record ItemRefeicaoResponse(
        Long id,
        String alimento,
        String quantidade,
        String unidadeMedida,
        String substituicoes,
        String observacoes,
        Integer ordem,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}