package br.com.nutricionista.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroNutricionistaRequest(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "Informe um email válido.")
        @Size(max = 150, message = "O email deve ter no máximo 150 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres.")
        String senha,

        @NotBlank(message = "O CRN é obrigatório.")
        @Size(max = 30, message = "O CRN deve ter no máximo 30 caracteres.")
        String crn
) {
}