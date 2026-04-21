package br.com.nutricionista.system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record HistoricoAntropometricoResponse(
        Long id,
        LocalDate dataAvaliacao,
        BigDecimal peso,
        BigDecimal imc,
        BigDecimal percentualGordura,
        BigDecimal somaDobras,
        BigDecimal circunferenciaCintura,
        BigDecimal circunferenciaQuadril,
        BigDecimal relacaoCinturaQuadril,
        BigDecimal relacaoCinturaEstatura,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}