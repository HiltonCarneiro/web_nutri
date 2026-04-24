package br.com.nutricionista.system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "tokens_redefinicao_senha",
        indexes = {
                @Index(name = "idx_token_redefinicao_hash", columnList = "token_hash", unique = true),
                @Index(name = "idx_token_redefinicao_expiracao", columnList = "data_expiracao")
        }
)
public class TokenRedefinicaoSenha extends BaseEntity {

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nutricionista_id", nullable = false)
    private Nutricionista nutricionista;

    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao;

    @Column(nullable = false)
    private Boolean usado = false;

    @Column(name = "usado_em")
    private LocalDateTime usadoEm;
}