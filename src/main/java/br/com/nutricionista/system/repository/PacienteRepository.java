package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    long countByNutricionistaIdAndAtivoTrue(Long nutricionistaId);

    List<Paciente> findTop5ByNutricionistaIdAndAtivoTrueOrderByCreatedAtDesc(Long nutricionistaId);

    @Query("""
            select count(p)
            from Paciente p
            where p.nutricionista.id = :nutricionistaId
              and p.ativo = true
              and not exists (
                    select d.id
                    from Dieta d
                    where d.paciente.id = p.id
                      and d.nutricionista.id = :nutricionistaId
                      and d.ativa = true
                      and d.ativo = true
              )
            """)
    long countPacientesSemDietaAtiva(@Param("nutricionistaId") Long nutricionistaId);
}