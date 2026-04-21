package br.com.nutricionista.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "dietas")
public class Dieta extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(name = "descricao_geral", columnDefinition = "TEXT")
    private String descricaoGeral;

    @Column(name = "objetivo_dieta", columnDefinition = "TEXT")
    private String objetivoDieta;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(nullable = false)
    private Boolean ativa = true;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nutricionista_id", nullable = false)
    private Nutricionista nutricionista;

    @JsonIgnore
    @OneToMany(mappedBy = "dieta")
    @OrderBy("ordem ASC, id ASC")
    private List<Refeicao> refeicoes = new ArrayList<>();
}