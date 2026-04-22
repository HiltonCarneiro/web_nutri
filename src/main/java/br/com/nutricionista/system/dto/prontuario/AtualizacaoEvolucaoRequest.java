package br.com.nutricionista.system.dto.prontuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AtualizacaoEvolucaoRequest(

        @NotNull(message = "A data da consulta é obrigatória.")
        @PastOrPresent(message = "A data da consulta não pode ser futura.")
        LocalDate dataConsulta,

        @NotBlank(message = "As observações da evolução são obrigatórias.")
        @Size(max = 5000, message = "As observações devem ter no máximo 5000 caracteres.")
        String observacoes,

        @Size(max = 5000, message = "As condutas devem ter no máximo 5000 caracteres.")
        String condutas,

        @Size(max = 5000, message = "As queixas do paciente devem ter no máximo 5000 caracteres.")
        String queixasPaciente,

        @Size(max = 5000, message = "As metas definidas devem ter no máximo 5000 caracteres.")
        String metasDefinidas
) {
}