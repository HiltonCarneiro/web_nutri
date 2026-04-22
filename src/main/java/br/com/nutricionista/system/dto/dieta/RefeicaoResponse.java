package br.com.nutricionista.system.dto.dieta;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record RefeicaoResponse(
        Long id,
        String nomeRefeicao,
        LocalTime horario,
        String descricao,
        String observacoes,
        Integer ordem,
        Boolean ativo,
        List<ItemRefeicaoResponse> itens,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}