package br.com.nutricionista.system.dto.login;

import br.com.nutricionista.system.dto.nutricionista.NutricionistaResponse;

public record LoginResponse(
        NutricionistaResponse nutricionista,
        String sessionId
) {
}