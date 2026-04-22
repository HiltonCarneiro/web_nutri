package br.com.nutricionista.system.dto.prontuario;

import br.com.nutricionista.system.dto.paciente.PacienteResponse;

import java.util.List;

public record ProntuarioPacienteResponse(
        PacienteResponse paciente,
        List<EvolucaoConsultaResponse> evolucoes
) {
}