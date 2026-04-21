package br.com.nutricionista.system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "avaliacoes_antropometricas")
public class AvaliacaoAntropometrica extends BaseEntity {

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate dataAvaliacao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal peso;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal altura;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal imc;

    @Column(name = "circunferencia_cintura", nullable = false, precision = 10, scale = 2)
    private BigDecimal circunferenciaCintura;

    @Column(name = "circunferencia_quadril", nullable = false, precision = 10, scale = 2)
    private BigDecimal circunferenciaQuadril;

    @Column(name = "circunferencia_braco", precision = 10, scale = 2)
    private BigDecimal circunferenciaBraco;

    @Column(name = "circunferencia_coxa", precision = 10, scale = 2)
    private BigDecimal circunferenciaCoxa;

    @Column(name = "circunferencia_panturrilha", precision = 10, scale = 2)
    private BigDecimal circunferenciaPanturrilha;

    @Column(name = "circunferencia_pescoco", precision = 10, scale = 2)
    private BigDecimal circunferenciaPescoco;

    @Column(name = "tricipital", nullable = false, precision = 10, scale = 2)
    private BigDecimal tricipital;

    @Column(name = "subescapular", nullable = false, precision = 10, scale = 2)
    private BigDecimal subescapular;

    @Column(name = "peitoral", nullable = false, precision = 10, scale = 2)
    private BigDecimal peitoral;

    @Column(name = "axilar_media", nullable = false, precision = 10, scale = 2)
    private BigDecimal axilarMedia;

    @Column(name = "suprailiaca", nullable = false, precision = 10, scale = 2)
    private BigDecimal suprailiaca;

    @Column(name = "abdominal", nullable = false, precision = 10, scale = 2)
    private BigDecimal abdominal;

    @Column(name = "coxa", nullable = false, precision = 10, scale = 2)
    private BigDecimal coxa;

    @Column(name = "soma_dobras", nullable = false, precision = 10, scale = 2)
    private BigDecimal somaDobras;

    @Column(name = "percentual_gordura", nullable = false, precision = 10, scale = 2)
    private BigDecimal percentualGordura;

    @Column(name = "relacao_cintura_quadril", precision = 10, scale = 4)
    private BigDecimal relacaoCinturaQuadril;

    @Column(name = "relacao_cintura_estatura", precision = 10, scale = 4)
    private BigDecimal relacaoCinturaEstatura;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nutricionista_id", nullable = false)
    private Nutricionista nutricionista;
}