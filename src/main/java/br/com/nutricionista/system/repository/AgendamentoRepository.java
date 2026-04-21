package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.Agendamento;
import br.com.nutricionista.system.entity.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Optional<Agendamento> findByIdAndPacienteIdAndNutricionistaIdAndAtivoTrue(
            Long agendamentoId,
            Long pacienteId,
            Long nutricionistaId
    );

    List<Agendamento> findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByDataHoraInicioDescIdDesc(
            Long pacienteId,
            Long nutricionistaId
    );

    List<Agendamento> findAllByNutricionistaIdAndAtivoTrueAndDataHoraInicioGreaterThanEqualAndDataHoraInicioLessThanOrderByDataHoraInicioAscIdAsc(
            Long nutricionistaId,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    boolean existsByNutricionistaIdAndAtivoTrueAndStatusInAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            Long nutricionistaId,
            Collection<StatusAgendamento> status,
            LocalDateTime dataHoraFim,
            LocalDateTime dataHoraInicio
    );

    boolean existsByNutricionistaIdAndAtivoTrueAndStatusInAndDataHoraInicioLessThanAndDataHoraFimGreaterThanAndIdNot(
            Long nutricionistaId,
            Collection<StatusAgendamento> status,
            LocalDateTime dataHoraFim,
            LocalDateTime dataHoraInicio,
            Long agendamentoId
    );
}