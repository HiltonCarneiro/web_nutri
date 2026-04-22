package br.com.nutricionista.system.dto.dieta;

import java.time.LocalDateTime;
import java.util.List;

public record DietaResponse(
        Long id,
        String titulo,
        String descricaoGeral,
        String objetivoDieta,
        String observacoes,
        Boolean ativa,
        Boolean ativo,
        List<RefeicaoResponse> refeicoes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}