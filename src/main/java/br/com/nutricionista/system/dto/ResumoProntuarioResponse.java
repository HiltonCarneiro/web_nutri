package br.com.nutricionista.system.dto;

import java.time.LocalDateTime;

public record ResumoProntuarioResponse(
        EvolucaoConsultaResponse ultimaEvolucao,
        AvaliacaoAntropometricaResponse ultimaAvaliacaoAntropometrica,
        DietaResumoResponse dietaAtivaAtual,
        LocalDateTime ultimaAtualizacaoProntuario
) {
}