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

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "itens_refeicao")
public class ItemRefeicao extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String alimento;

    @Column(nullable = false, length = 50)
    private String quantidade;

    @Column(name = "unidade_medida", length = 50)
    private String unidadeMedida;

    @Column(columnDefinition = "TEXT")
    private String substituicoes;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "refeicao_id", nullable = false)
    private Refeicao refeicao;
}