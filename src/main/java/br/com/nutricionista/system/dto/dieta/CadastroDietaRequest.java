package br.com.nutricionista.system.dto.dieta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CadastroDietaRequest(

        @NotBlank(message = "O título da dieta é obrigatório.")
        @Size(max = 150, message = "O título da dieta deve ter no máximo 150 caracteres.")
        String titulo,

        @Size(max = 5000, message = "A descrição geral deve ter no máximo 5000 caracteres.")
        String descricaoGeral,

        @Size(max = 5000, message = "O objetivo da dieta deve ter no máximo 5000 caracteres.")
        String objetivoDieta,

        @Size(max = 5000, message = "As observações devem ter no máximo 5000 caracteres.")
        String observacoes,

        @Valid
        List<CadastroRefeicaoRequest> refeicoes
) {
}