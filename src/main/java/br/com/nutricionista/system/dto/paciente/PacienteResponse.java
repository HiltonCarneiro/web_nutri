package br.com.nutricionista.system.dto.paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PacienteResponse(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String sexo,
        String telefone,
        String email,
        String endereco,
        String observacoes,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}