package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.Dieta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DietaRepository extends JpaRepository<Dieta, Long> {

    List<Dieta> findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByCreatedAtDescIdDesc(
            Long pacienteId,
            Long nutricionistaId
    );

    Optional<Dieta> findByIdAndPacienteIdAndNutricionistaIdAndAtivoTrue(
            Long dietaId,
            Long pacienteId,
            Long nutricionistaId
    );

    List<Dieta> findAllByPacienteIdAndNutricionistaIdAndAtivaTrueAndAtivoTrue(
            Long pacienteId,
            Long nutricionistaId
    );

    long countByNutricionistaIdAndAtivaTrueAndAtivoTrue(Long nutricionistaId);
}