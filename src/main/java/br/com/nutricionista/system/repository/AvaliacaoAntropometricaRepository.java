package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.AvaliacaoAntropometrica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvaliacaoAntropometricaRepository extends JpaRepository<AvaliacaoAntropometrica, Long> {

    List<AvaliacaoAntropometrica> findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByDataAvaliacaoDescIdDesc(
            Long pacienteId,
            Long nutricionistaId
    );

    Optional<AvaliacaoAntropometrica> findByIdAndPacienteIdAndNutricionistaIdAndAtivoTrue(
            Long avaliacaoId,
            Long pacienteId,
            Long nutricionistaId
    );
}