package br.com.nutricionista.system.dto.paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AtualizacaoPacienteRequest(

        @NotBlank(message = "O nome do paciente é obrigatório.")
        @Size(max = 150, message = "O nome do paciente deve ter no máximo 150 caracteres.")
        String nome,

        @NotBlank(message = "O CPF é obrigatório.")
        @Pattern(
                regexp = "^\\d{11}$|^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$",
                message = "Informe um CPF válido com 11 dígitos."
        )
        String cpf,

        @NotNull(message = "A data de nascimento é obrigatória.")
        @Past(message = "A data de nascimento deve ser uma data passada.")
        LocalDate dataNascimento,

        @NotBlank(message = "O sexo é obrigatório.")
        @Size(max = 20, message = "O sexo deve ter no máximo 20 caracteres.")
        String sexo,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
        String telefone,

        @Email(message = "Informe um email válido.")
        @Size(max = 150, message = "O email deve ter no máximo 150 caracteres.")
        String email,

        @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres.")
        String endereco,

        @Size(max = 2000, message = "As observações devem ter no máximo 2000 caracteres.")
        String observacoes
) {
}