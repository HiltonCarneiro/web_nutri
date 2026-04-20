package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByIdAndNutricionistaIdAndAtivoTrue(Long id, Long nutricionistaId);

    List<Paciente> findAllByNutricionistaIdAndAtivoTrueOrderByNomeAsc(Long nutricionistaId);

    List<Paciente> findByNutricionistaIdAndAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(Long nutricionistaId, String nome);

    boolean existsByNutricionistaIdAndCpfAndAtivoTrue(Long nutricionistaId, String cpf);

    boolean existsByNutricionistaIdAndCpfAndAtivoTrueAndIdNot(Long nutricionistaId, String cpf, Long id);

    boolean existsByNutricionistaIdAndEmailIgnoreCaseAndAtivoTrue(Long nutricionistaId, String email);

    boolean existsByNutricionistaIdAndEmailIgnoreCaseAndAtivoTrueAndIdNot(Long nutricionistaId, String email, Long id);
}