package br.com.nutricionista.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SolicitarRedefinicaoSenhaRequest(

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "Informe um email válido.")
        @Size(max = 150, message = "O email deve ter no máximo 150 caracteres.")
        String email
) {
}