package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.Refeicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefeicaoRepository extends JpaRepository<Refeicao, Long> {

    List<Refeicao> findAllByDietaIdAndAtivoTrueOrderByOrdemAscIdAsc(Long dietaId);

    Optional<Refeicao> findByIdAndDietaIdAndDietaPacienteIdAndDietaNutricionistaIdAndAtivoTrue(
            Long refeicaoId,
            Long dietaId,
            Long pacienteId,
            Long nutricionistaId
    );
}