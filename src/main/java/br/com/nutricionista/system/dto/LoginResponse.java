package br.com.nutricionista.system.dto;

public record LoginResponse(
        NutricionistaResponse nutricionista,
        String sessionId
) {
}