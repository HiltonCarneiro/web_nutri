package br.com.nutricionista.system.dto.prontuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AvaliacaoAntropometricaResponse(
        Long id,
        LocalDate dataAvaliacao,
        BigDecimal peso,
        BigDecimal altura,
        BigDecimal imc,
        BigDecimal circunferenciaCintura,
        BigDecimal circunferenciaQuadril,
        BigDecimal circunferenciaBraco,
        BigDecimal circunferenciaCoxa,
        BigDecimal circunferenciaPanturrilha,
        BigDecimal circunferenciaPescoco,
        BigDecimal tricipital,
        BigDecimal subescapular,
        BigDecimal peitoral,
        BigDecimal axilarMedia,
        BigDecimal suprailiaca,
        BigDecimal abdominal,
        BigDecimal coxa,
        BigDecimal somaDobras,
        BigDecimal percentualGordura,
        BigDecimal relacaoCinturaQuadril,
        BigDecimal relacaoCinturaEstatura,
        String observacoes,
        Boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}