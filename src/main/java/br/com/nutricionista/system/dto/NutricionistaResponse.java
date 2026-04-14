package br.com.nutricionista.system.dto;

public record NutricionistaResponse(
        Long id,
        String nome,
        String email,
        String crn,
        Boolean ativo
) {
}