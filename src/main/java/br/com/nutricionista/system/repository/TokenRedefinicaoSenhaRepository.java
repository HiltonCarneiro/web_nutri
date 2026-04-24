package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.TokenRedefinicaoSenha;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRedefinicaoSenhaRepository extends JpaRepository<TokenRedefinicaoSenha, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TokenRedefinicaoSenha> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
            update TokenRedefinicaoSenha t
               set t.usado = true,
                   t.usadoEm = :usadoEm
             where t.nutricionista.id = :nutricionistaId
               and t.usado = false
            """)
    int invalidarTokensPendentesDoNutricionista(
            @Param("nutricionistaId") Long nutricionistaId,
            @Param("usadoEm") LocalDateTime usadoEm
    );
}