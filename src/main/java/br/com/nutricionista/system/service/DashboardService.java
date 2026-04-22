package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.agendamento.AgendamentoResponse;
import br.com.nutricionista.system.dto.dashboard.*;
import br.com.nutricionista.system.entity.Nutricionista;
import br.com.nutricionista.system.entity.StatusAgendamento;
import br.com.nutricionista.system.exception.BusinessException;
import br.com.nutricionista.system.repository.AgendamentoRepository;
import br.com.nutricionista.system.repository.DietaRepository;
import br.com.nutricionista.system.repository.PacienteRepository;
import br.com.nutricionista.system.security.SistemaUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PacienteRepository pacienteRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final DietaRepository dietaRepository;

    public DashboardResponse obterDashboard() {

        Nutricionista nutricionista = getNutricionistaAutenticado();

        Long nutricionistaId = nutricionista.getId();

        LocalDate hoje = LocalDate.now();
        LocalDateTime inicio = hoje.atStartOfDay();
        LocalDateTime fim = hoje.plusDays(1).atStartOfDay();

        // ===== RESUMO =====
        Long totalPacientes = pacienteRepository.countByNutricionistaIdAndAtivoTrue(nutricionistaId);

        List<AgendamentoResponse> agendamentosHoje =
                agendamentoRepository
                        .findAllByNutricionistaIdAndAtivoTrueAndDataHoraInicioGreaterThanEqualAndDataHoraInicioLessThanOrderByDataHoraInicioAscIdAsc(
                                nutricionistaId,
                                inicio,
                                fim
                        )
                        .stream()
                        .map(a -> new AgendamentoResponse(
                                a.getId(),
                                a.getPaciente().getId(),
                                a.getPaciente().getNome(),
                                a.getDataHoraInicio(),
                                a.getDataHoraFim(),
                                a.getStatus(),
                                a.getTipoConsulta(),
                                a.getObservacoes(),
                                a.getMotivoCancelamento(),
                                a.getRealizadoEm(),
                                a.getAtivo(),
                                a.getCreatedAt(),
                                a.getUpdatedAt()
                        ))
                        .toList();

        Long totalAgendamentosHoje = (long) agendamentosHoje.size();

        Long totalPendentesHoje = agendamentosHoje.stream()
                .filter(a -> a.status() == StatusAgendamento.AGENDADO || a.status() == StatusAgendamento.CONFIRMADO)
                .count();

        Long totalDietasAtivas =
                dietaRepository.countByNutricionistaIdAndAtivaTrueAndAtivoTrue(nutricionistaId);

        DashboardResumoResponse resumo = new DashboardResumoResponse(
                totalPacientes,
                totalAgendamentosHoje,
                totalPendentesHoje,
                totalDietasAtivas
        );

        // ===== PRÓXIMOS AGENDAMENTOS =====
        List<DashboardAgendamentoResponse> proximosAgendamentos =
                agendamentosHoje.stream()
                        .limit(5)
                        .map(a -> new DashboardAgendamentoResponse(
                                a.id(),
                                a.nomePaciente(),
                                a.dataHoraInicio(),
                                a.status(),
                                a.tipoConsulta()
                        ))
                        .toList();

        // ===== PACIENTES RECENTES =====
        List<DashboardPacienteResponse> pacientesRecentes =
                pacienteRepository
                        .findTop5ByNutricionistaIdAndAtivoTrueOrderByCreatedAtDesc(nutricionistaId)
                        .stream()
                        .map(p -> new DashboardPacienteResponse(
                                p.getId(),
                                p.getNome(),
                                p.getCreatedAt()
                        ))
                        .toList();

        // ===== ALERTAS =====
        Long pacientesSemDieta =
                pacienteRepository.countPacientesSemDietaAtiva(nutricionistaId);

        List<DashboardAlertaResponse> alertas = List.of(
                new DashboardAlertaResponse("PACIENTES_SEM_DIETA", pacientesSemDieta)
        );

        return new DashboardResponse(
                resumo,
                proximosAgendamentos,
                pacientesRecentes,
                alertas
        );
    }

    private Nutricionista getNutricionistaAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication.getPrincipal() instanceof SistemaUserDetails userDetails)) {
            throw new BusinessException("Usuário inválido");
        }

        return userDetails.getNutricionista();
    }
}