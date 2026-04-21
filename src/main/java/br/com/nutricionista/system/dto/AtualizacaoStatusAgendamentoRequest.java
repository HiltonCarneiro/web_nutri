package br.com.nutricionista.system.dto;

import br.com.nutricionista.system.entity.StatusAgendamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AtualizacaoStatusAgendamentoRequest(

        @NotNull(message = "O status do agendamento é obrigatório.")
        StatusAgendamento status,

        @Size(max = 5000, message = "O motivo do cancelamento deve ter no máximo 5000 caracteres.")
        String motivoCancelamento
) {
}