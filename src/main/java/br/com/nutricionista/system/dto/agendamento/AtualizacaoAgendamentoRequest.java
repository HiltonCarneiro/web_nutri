package br.com.nutricionista.system.dto.agendamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AtualizacaoAgendamentoRequest(

        @NotNull(message = "A data e hora de início são obrigatórias.")
        LocalDateTime dataHoraInicio,

        @NotNull(message = "A data e hora de fim são obrigatórias.")
        LocalDateTime dataHoraFim,

        @NotBlank(message = "O tipo de consulta é obrigatório.")
        @Size(max = 100, message = "O tipo de consulta deve ter no máximo 100 caracteres.")
        String tipoConsulta,

        @Size(max = 5000, message = "As observações devem ter no máximo 5000 caracteres.")
        String observacoes
) {
}