package br.com.nutricionista.system.dto.agendamento;

import br.com.nutricionista.system.entity.StatusAgendamento;

import java.time.LocalDateTime;

public record AgendamentoResponse(
        Long id,
        Long pacienteId,
        String nomePaciente,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        StatusAgendamento status,
        String tipoConsulta,
        String observacoes,
        String motivoCancelamento,
        LocalDateTime realizadoEm,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}