package br.com.nutricionista.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RedefinirSenhaRequest(

        @NotBlank(message = "O token é obrigatório.")
        String token,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 8, max = 100, message = "A nova senha deve ter entre 8 e 100 caracteres.")
        String novaSenha,

        @NotBlank(message = "A confirmação da nova senha é obrigatória.")
        @Size(min = 8, max = 100, message = "A confirmação da nova senha deve ter entre 8 e 100 caracteres.")
        String confirmacaoNovaSenha
) {
}