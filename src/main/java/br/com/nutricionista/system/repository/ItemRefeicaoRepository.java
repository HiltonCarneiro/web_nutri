package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.ItemRefeicao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRefeicaoRepository extends JpaRepository<ItemRefeicao, Long> {

    List<ItemRefeicao> findAllByRefeicaoIdAndAtivoTrueOrderByOrdemAscIdAsc(Long refeicaoId);

    Optional<ItemRefeicao> findByIdAndRefeicaoIdAndRefeicaoDietaIdAndRefeicaoDietaPacienteIdAndRefeicaoDietaNutricionistaIdAndAtivoTrue(
            Long itemId,
            Long refeicaoId,
            Long dietaId,
            Long pacienteId,
            Long nutricionistaId
    );
}