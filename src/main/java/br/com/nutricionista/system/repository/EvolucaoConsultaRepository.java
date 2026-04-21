package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.EvolucaoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvolucaoConsultaRepository extends JpaRepository<EvolucaoConsulta, Long> {

    List<EvolucaoConsulta> findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByDataConsultaDescIdDesc(
            Long pacienteId,
            Long nutricionistaId
    );

    Optional<EvolucaoConsulta> findByIdAndPacienteIdAndNutricionistaIdAndAtivoTrue(
            Long evolucaoId,
            Long pacienteId,
            Long nutricionistaId
    );
}