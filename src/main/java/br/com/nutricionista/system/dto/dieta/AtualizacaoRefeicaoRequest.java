package br.com.nutricionista.system.dto.dieta;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record AtualizacaoRefeicaoRequest(

        @NotBlank(message = "O nome da refeição é obrigatório.")
        @Size(max = 120, message = "O nome da refeição deve ter no máximo 120 caracteres.")
        String nomeRefeicao,

        LocalTime horario,

        @Size(max = 3000, message = "A descrição deve ter no máximo 3000 caracteres.")
        String descricao,

        @Size(max = 3000, message = "As observações devem ter no máximo 3000 caracteres.")
        String observacoes,

        @NotNull(message = "A ordem da refeição é obrigatória.")
        @Min(value = 1, message = "A ordem da refeição deve ser maior que zero.")
        Integer ordem
) {
}