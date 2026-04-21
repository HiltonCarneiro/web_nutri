package br.com.nutricionista.system.dto;

import java.util.List;

public record ProntuarioCompletoResponse(
        PacienteResponse paciente,
        ResumoProntuarioResponse resumo,
        List<EvolucaoConsultaResponse> evolucoes,
        List<AvaliacaoAntropometricaResponse> avaliacoesAntropometricas,
        List<HistoricoAntropometricoResponse> historicoAntropometrico,
        DietaResponse dietaAtiva,
        List<DietaResumoResponse> dietas,
        List<HistoricoDietaResponse> historicoDietas
) {
}