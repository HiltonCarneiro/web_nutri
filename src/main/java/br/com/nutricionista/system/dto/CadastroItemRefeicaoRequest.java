package br.com.nutricionista.system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CadastroItemRefeicaoRequest(

        @NotBlank(message = "O alimento é obrigatório.")
        @Size(max = 200, message = "O alimento deve ter no máximo 200 caracteres.")
        String alimento,

        @NotBlank(message = "A quantidade é obrigatória.")
        @Size(max = 50, message = "A quantidade deve ter no máximo 50 caracteres.")
        String quantidade,

        @Size(max = 50, message = "A unidade de medida deve ter no máximo 50 caracteres.")
        String unidadeMedida,

        @Size(max = 3000, message = "As substituições devem ter no máximo 3000 caracteres.")
        String substituicoes,

        @Size(max = 3000, message = "As observações devem ter no máximo 3000 caracteres.")
        String observacoes,

        @NotNull(message = "A ordem do item é obrigatória.")
        @Min(value = 1, message = "A ordem do item deve ser maior que zero.")
        Integer ordem
) {
}