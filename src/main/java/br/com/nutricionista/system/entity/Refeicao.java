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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "refeicoes")
public class Refeicao extends BaseEntity {

    @Column(name = "nome_refeicao", nullable = false, length = 120)
    private String nomeRefeicao;

    @Column
    private LocalTime horario;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(nullable = false)
    private Integer ordem;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dieta_id", nullable = false)
    private Dieta dieta;

    @JsonIgnore
    @OneToMany(mappedBy = "refeicao")
    @OrderBy("ordem ASC, id ASC")
    private List<ItemRefeicao> itens = new ArrayList<>();
}