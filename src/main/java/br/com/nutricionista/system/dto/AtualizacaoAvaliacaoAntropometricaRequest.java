package br.com.nutricionista.system.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AtualizacaoAvaliacaoAntropometricaRequest(

        @NotNull(message = "A data da avaliação é obrigatória.")
        @PastOrPresent(message = "A data da avaliação não pode ser futura.")
        LocalDate dataAvaliacao,

        @NotNull(message = "O peso é obrigatório.")
        @DecimalMin(value = "0.1", message = "O peso deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "O peso deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal peso,

        @NotNull(message = "A altura é obrigatória.")
        @DecimalMin(value = "0.1", message = "A altura deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A altura deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal altura,

        @NotNull(message = "A circunferência da cintura é obrigatória.")
        @DecimalMin(value = "0.1", message = "A circunferência da cintura deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A circunferência da cintura deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal circunferenciaCintura,

        @NotNull(message = "A circunferência do quadril é obrigatória.")
        @DecimalMin(value = "0.1", message = "A circunferência do quadril deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A circunferência do quadril deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal circunferenciaQuadril,

        @Digits(integer = 8, fraction = 2, message = "A circunferência do braço deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        @DecimalMin(value = "0.1", inclusive = false, message = "A circunferência do braço deve ser maior que zero.")
        BigDecimal circunferenciaBraco,

        @Digits(integer = 8, fraction = 2, message = "A circunferência da coxa deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        @DecimalMin(value = "0.1", inclusive = false, message = "A circunferência da coxa deve ser maior que zero.")
        BigDecimal circunferenciaCoxa,

        @Digits(integer = 8, fraction = 2, message = "A circunferência da panturrilha deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        @DecimalMin(value = "0.1", inclusive = false, message = "A circunferência da panturrilha deve ser maior que zero.")
        BigDecimal circunferenciaPanturrilha,

        @Digits(integer = 8, fraction = 2, message = "A circunferência do pescoço deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        @DecimalMin(value = "0.1", inclusive = false, message = "A circunferência do pescoço deve ser maior que zero.")
        BigDecimal circunferenciaPescoco,

        @NotNull(message = "A dobra tricipital é obrigatória.")
        @DecimalMin(value = "0.1", message = "A dobra tricipital deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A dobra tricipital deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal tricipital,

        @NotNull(message = "A dobra subescapular é obrigatória.")
        @DecimalMin(value = "0.1", message = "A dobra subescapular deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A dobra subescapular deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal subescapular,

        @NotNull(message = "A dobra peitoral é obrigatória.")
        @DecimalMin(value = "0.1", message = "A dobra peitoral deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A dobra peitoral deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal peitoral,

        @NotNull(message = "A dobra axilar média é obrigatória.")
        @DecimalMin(value = "0.1", message = "A dobra axilar média deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A dobra axilar média deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal axilarMedia,

        @NotNull(message = "A dobra supra-ilíaca é obrigatória.")
        @DecimalMin(value = "0.1", message = "A dobra supra-ilíaca deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A dobra supra-ilíaca deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal suprailiaca,

        @NotNull(message = "A dobra abdominal é obrigatória.")
        @DecimalMin(value = "0.1", message = "A dobra abdominal deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A dobra abdominal deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal abdominal,

        @NotNull(message = "A dobra da coxa é obrigatória.")
        @DecimalMin(value = "0.1", message = "A dobra da coxa deve ser maior que zero.")
        @Digits(integer = 8, fraction = 2, message = "A dobra da coxa deve ter no máximo 8 dígitos inteiros e 2 decimais.")
        BigDecimal coxa,

        @Size(max = 2000, message = "As observações devem ter no máximo 2000 caracteres.")
        String observacoes
) {
}