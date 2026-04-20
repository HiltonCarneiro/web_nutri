package br.com.nutricionista.system.dto;

import java.util.List;

public record ProntuarioPacienteResponse(
        PacienteResponse paciente,
        List<EvolucaoConsultaResponse> evolucoes
) {
}