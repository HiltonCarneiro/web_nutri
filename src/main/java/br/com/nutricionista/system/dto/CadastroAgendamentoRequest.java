package br.com.nutricionista.system.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CadastroAgendamentoRequest(

        @NotNull(message = "A data e hora de início são obrigatórias.")
        @FutureOrPresent(message = "A data e hora de início devem ser atuais ou futuras.")
        LocalDateTime dataHoraInicio,

        @NotNull(message = "A data e hora de fim são obrigatórias.")
        @Future(message = "A data e hora de fim deve ser futura.")
        LocalDateTime dataHoraFim,

        @NotBlank(message = "O tipo de consulta é obrigatório.")
        @Size(max = 100, message = "O tipo de consulta deve ter no máximo 100 caracteres.")
        String tipoConsulta,

        @Size(max = 5000, message = "As observações devem ter no máximo 5000 caracteres.")
        String observacoes
) {
}