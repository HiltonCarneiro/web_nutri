package br.com.nutricionista.system.dto;

import jakarta.validation.constraints.Size;

public record CancelamentoAgendamentoRequest(

        @Size(max = 5000, message = "O motivo do cancelamento deve ter no máximo 5000 caracteres.")
        String motivoCancelamento
) {
}