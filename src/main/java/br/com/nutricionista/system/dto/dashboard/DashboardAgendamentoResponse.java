package br.com.nutricionista.system.dto.dashboard;

import br.com.nutricionista.system.entity.StatusAgendamento;

import java.time.LocalDateTime;

public record DashboardAgendamentoResponse(
        Long id,
        String nomePaciente,
        LocalDateTime dataHoraInicio,
        StatusAgendamento status,
        String tipoConsulta
) {
}