package br.com.nutricionista.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtualizacaoDietaRequest(

        @NotBlank(message = "O título da dieta é obrigatório.")
        @Size(max = 150, message = "O título da dieta deve ter no máximo 150 caracteres.")
        String titulo,

        @Size(max = 5000, message = "A descrição geral deve ter no máximo 5000 caracteres.")
        String descricaoGeral,

        @Size(max = 5000, message = "O objetivo da dieta deve ter no máximo 5000 caracteres.")
        String objetivoDieta,

        @Size(max = 5000, message = "As observações devem ter no máximo 5000 caracteres.")
        String observacoes,

        @NotNull(message = "O status ativo da dieta é obrigatório.")
        Boolean ativa
) {
}