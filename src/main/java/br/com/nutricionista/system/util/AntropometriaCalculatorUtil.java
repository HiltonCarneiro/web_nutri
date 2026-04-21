package br.com.nutricionista.system.util;

import br.com.nutricionista.system.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Locale;

@Component
public class AntropometriaCalculatorUtil {

    private static final MathContext MATH_CONTEXT = new MathContext(12, RoundingMode.HALF_UP);

    public BigDecimal calcularImc(BigDecimal peso, BigDecimal altura) {
        validarMaiorQueZero(peso, "peso");
        validarMaiorQueZero(altura, "altura");

        BigDecimal alturaAoQuadrado = altura.multiply(altura, MATH_CONTEXT);
        return peso.divide(alturaAoQuadrado, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularSomaDobras(
            BigDecimal tricipital,
            BigDecimal subescapular,
            BigDecimal peitoral,
            BigDecimal axilarMedia,
            BigDecimal suprailiaca,
            BigDecimal abdominal,
            BigDecimal coxa
    ) {
        validarMaiorQueZero(tricipital, "tricipital");
        validarMaiorQueZero(subescapular, "subescapular");
        validarMaiorQueZero(peitoral, "peitoral");
        validarMaiorQueZero(axilarMedia, "axilarMedia");
        validarMaiorQueZero(suprailiaca, "suprailiaca");
        validarMaiorQueZero(abdominal, "abdominal");
        validarMaiorQueZero(coxa, "coxa");

        return tricipital
                .add(subescapular)
                .add(peitoral)
                .add(axilarMedia)
                .add(suprailiaca)
                .add(abdominal)
                .add(coxa)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularPercentualGordura(
            String sexo,
            LocalDate dataNascimento,
            LocalDate dataAvaliacao,
            BigDecimal somaDobras
    ) {
        validarMaiorQueZero(somaDobras, "somaDobras");

        int idade = calcularIdadeNaData(dataNascimento, dataAvaliacao);
        BigDecimal densidadeCorporal = calcularDensidadeCorporal(sexo, somaDobras, idade);

        if (densidadeCorporal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    "Não foi possível calcular o percentual de gordura com os dados informados.",
                    HttpStatus.BAD_REQUEST
            );
        }

        BigDecimal percentual = BigDecimal.valueOf(495)
                .divide(densidadeCorporal, 10, RoundingMode.HALF_UP)
                .subtract(BigDecimal.valueOf(450));

        if (percentual.compareTo(BigDecimal.ZERO) < 0) {
            percentual = BigDecimal.ZERO;
        }

        return percentual.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularRelacaoCinturaQuadril(BigDecimal cintura, BigDecimal quadril) {
        validarMaiorQueZero(cintura, "circunferenciaCintura");
        validarMaiorQueZero(quadril, "circunferenciaQuadril");
        return cintura.divide(quadril, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularRelacaoCinturaEstatura(BigDecimal cintura, BigDecimal altura) {
        validarMaiorQueZero(cintura, "circunferenciaCintura");
        validarMaiorQueZero(altura, "altura");

        BigDecimal alturaEmCentimetros = altura.multiply(BigDecimal.valueOf(100), MATH_CONTEXT);
        return cintura.divide(alturaEmCentimetros, 4, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularDensidadeCorporal(String sexo, BigDecimal somaDobras, int idade) {
        BigDecimal somaQuadrado = somaDobras.multiply(somaDobras, MATH_CONTEXT);
        String sexoNormalizado = normalizarSexo(sexo);

        if ("MASCULINO".equals(sexoNormalizado)) {
            return BigDecimal.valueOf(1.112)
                    .subtract(BigDecimal.valueOf(0.00043499).multiply(somaDobras, MATH_CONTEXT))
                    .add(BigDecimal.valueOf(0.00000055).multiply(somaQuadrado, MATH_CONTEXT))
                    .subtract(BigDecimal.valueOf(0.00028826).multiply(BigDecimal.valueOf(idade), MATH_CONTEXT));
        }

        if ("FEMININO".equals(sexoNormalizado)) {
            return BigDecimal.valueOf(1.097)
                    .subtract(BigDecimal.valueOf(0.00046971).multiply(somaDobras, MATH_CONTEXT))
                    .add(BigDecimal.valueOf(0.00000056).multiply(somaQuadrado, MATH_CONTEXT))
                    .subtract(BigDecimal.valueOf(0.00012828).multiply(BigDecimal.valueOf(idade), MATH_CONTEXT));
        }

        throw new BusinessException(
                "Sexo do paciente inválido para cálculo antropométrico. Use Masculino ou Feminino.",
                HttpStatus.BAD_REQUEST
        );
    }

    private int calcularIdadeNaData(LocalDate dataNascimento, LocalDate dataAvaliacao) {
        if (dataNascimento == null || dataAvaliacao == null) {
            throw new BusinessException(
                    "Data de nascimento e data da avaliação são obrigatórias para o cálculo antropométrico.",
                    HttpStatus.BAD_REQUEST
            );
        }

        if (dataAvaliacao.isBefore(dataNascimento)) {
            throw new BusinessException(
                    "A data da avaliação não pode ser anterior à data de nascimento do paciente.",
                    HttpStatus.BAD_REQUEST
            );
        }

        int idade = Period.between(dataNascimento, dataAvaliacao).getYears();
        if (idade < 1) {
            throw new BusinessException(
                    "Não foi possível calcular o percentual de gordura para idade inferior a 1 ano.",
                    HttpStatus.BAD_REQUEST
            );
        }

        return idade;
    }

    private String normalizarSexo(String sexo) {
        if (sexo == null) {
            return "";
        }

        String valor = sexo.trim().toUpperCase(Locale.ROOT);
        if (valor.equals("M") || valor.equals("MASCULINO") || valor.equals("HOMEM")) {
            return "MASCULINO";
        }
        if (valor.equals("F") || valor.equals("FEMININO") || valor.equals("MULHER")) {
            return "FEMININO";
        }
        return valor;
    }

    private void validarMaiorQueZero(BigDecimal valor, String campo) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(
                    "O campo " + campo + " deve ser maior que zero.",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}