package br.com.nutricionista.system.dto.prontuario;

import br.com.nutricionista.system.dto.dieta.HistoricoDietaResponse;
import br.com.nutricionista.system.dto.paciente.PacienteResponse;
import br.com.nutricionista.system.dto.dieta.DietaResponse;
import br.com.nutricionista.system.dto.dieta.DietaResumoResponse;

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