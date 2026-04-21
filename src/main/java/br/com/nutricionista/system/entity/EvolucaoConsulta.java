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

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "evolucoes_consulta")
public class EvolucaoConsulta extends BaseEntity {

    @Column(name = "data_consulta", nullable = false)
    private LocalDate dataConsulta;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String observacoes;

    @Column(columnDefinition = "TEXT")
    private String condutas;

    @Column(name = "queixas_paciente", columnDefinition = "TEXT")
    private String queixasPaciente;

    @Column(name = "metas_definidas", columnDefinition = "TEXT")
    private String metasDefinidas;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nutricionista_id", nullable = false)
    private Nutricionista nutricionista;
}