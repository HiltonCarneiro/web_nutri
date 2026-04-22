package br.com.nutricionista.system.dto.prontuario;

import br.com.nutricionista.system.dto.dieta.DietaResumoResponse;

import java.time.LocalDateTime;

public record ResumoProntuarioResponse(
        EvolucaoConsultaResponse ultimaEvolucao,
        AvaliacaoAntropometricaResponse ultimaAvaliacaoAntropometrica,
        DietaResumoResponse dietaAtivaAtual,
        LocalDateTime ultimaAtualizacaoProntuario
) {
}