package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.AgendamentoResponse;
import br.com.nutricionista.system.dto.AtualizacaoAgendamentoRequest;
import br.com.nutricionista.system.dto.AtualizacaoStatusAgendamentoRequest;
import br.com.nutricionista.system.dto.CadastroAgendamentoRequest;
import br.com.nutricionista.system.dto.CancelamentoAgendamentoRequest;
import br.com.nutricionista.system.entity.Agendamento;
import br.com.nutricionista.system.entity.Nutricionista;
import br.com.nutricionista.system.entity.Paciente;
import br.com.nutricionista.system.entity.StatusAgendamento;
import br.com.nutricionista.system.exception.BusinessException;
import br.com.nutricionista.system.exception.ResourceNotFoundException;
import br.com.nutricionista.system.repository.AgendamentoRepository;
import br.com.nutricionista.system.repository.PacienteRepository;
import br.com.nutricionista.system.security.SistemaUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private static final EnumSet<StatusAgendamento> STATUS_QUE_BLOQUEIAM_AGENDA = EnumSet.of(
            StatusAgendamento.AGENDADO,
            StatusAgendamento.CONFIRMADO
    );

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public AgendamentoResponse cadastrar(Long pacienteId, CadastroAgendamentoRequest request) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        validarPeriodo(request.dataHoraInicio(), request.dataHoraFim());
        validarConflitoHorario(nutricionista.getId(), request.dataHoraInicio(), request.dataHoraFim(), null);

        Agendamento agendamento = new Agendamento();
        preencherDadosAgendamento(
                agendamento,
                paciente,
                nutricionista,
                request.dataHoraInicio(),
                request.dataHoraFim(),
                request.tipoConsulta(),
                request.observacoes()
        );
        agendamento.setStatus(StatusAgendamento.AGENDADO);
        agendamento.setAtivo(Boolean.TRUE);
        agendamento.setMotivoCancelamento(null);
        agendamento.setRealizadoEm(null);

        return toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoResponse editar(Long pacienteId, Long agendamentoId, AtualizacaoAgendamentoRequest request) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());
        Agendamento agendamento = buscarAgendamentoAtivoDoPaciente(
                agendamentoId,
                pacienteId,
                nutricionista.getId()
        );

        validarAgendamentoEditavel(agendamento);
        validarPeriodo(request.dataHoraInicio(), request.dataHoraFim());
        validarConflitoHorario(
                nutricionista.getId(),
                request.dataHoraInicio(),
                request.dataHoraFim(),
                agendamentoId
        );

        preencherDadosAgendamento(
                agendamento,
                paciente,
                nutricionista,
                request.dataHoraInicio(),
                request.dataHoraFim(),
                request.tipoConsulta(),
                request.observacoes()
        );

        return toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoResponse cancelar(
            Long pacienteId,
            Long agendamentoId,
            CancelamentoAgendamentoRequest request
    ) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Agendamento agendamento = buscarAgendamentoAtivoDoPaciente(
                agendamentoId,
                pacienteId,
                nutricionista.getId()
        );

        if (StatusAgendamento.REALIZADO.equals(agendamento.getStatus())) {
            throw new BusinessException("Não é possível cancelar um agendamento já realizado.");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamento.setMotivoCancelamento(normalizarTexto(request.motivoCancelamento()));
        agendamento.setRealizadoEm(null);

        return toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional
    public AgendamentoResponse atualizarStatus(
            Long pacienteId,
            Long agendamentoId,
            AtualizacaoStatusAgendamentoRequest request
    ) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Agendamento agendamento = buscarAgendamentoAtivoDoPaciente(
                agendamentoId,
                pacienteId,
                nutricionista.getId()
        );

        validarTransicaoStatus(agendamento, request.status());

        if (EnumSet.of(StatusAgendamento.AGENDADO, StatusAgendamento.CONFIRMADO).contains(request.status())) {
            validarConflitoHorario(
                    nutricionista.getId(),
                    agendamento.getDataHoraInicio(),
                    agendamento.getDataHoraFim(),
                    agendamentoId
            );
        }

        agendamento.setStatus(request.status());

        if (StatusAgendamento.CANCELADO.equals(request.status())) {
            agendamento.setMotivoCancelamento(normalizarTexto(request.motivoCancelamento()));
            agendamento.setRealizadoEm(null);
        } else {
            agendamento.setMotivoCancelamento(null);
            agendamento.setRealizadoEm(StatusAgendamento.REALIZADO.equals(request.status()) ? LocalDateTime.now() : null);
        }

        return toResponse(agendamentoRepository.save(agendamento));
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarAgendaDoDia(LocalDate data) {
        LocalDate referencia = data != null ? data : LocalDate.now();
        return listarPorPeriodo(referencia.atStartOfDay(), referencia.plusDays(1).atStartOfDay());
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        validarPeriodoConsulta(inicio, fim);

        return agendamentoRepository
                .findAllByNutricionistaIdAndAtivoTrueAndDataHoraInicioGreaterThanEqualAndDataHoraInicioLessThanOrderByDataHoraInicioAscIdAsc(
                        nutricionista.getId(),
                        inicio,
                        fim
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarPorPaciente(Long pacienteId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        return agendamentoRepository
                .findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByDataHoraInicioDescIdDesc(
                        pacienteId,
                        nutricionista.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarPorId(Long pacienteId, Long agendamentoId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Agendamento agendamento = buscarAgendamentoAtivoDoPaciente(
                agendamentoId,
                pacienteId,
                nutricionista.getId()
        );
        return toResponse(agendamento);
    }

    private void preencherDadosAgendamento(
            Agendamento agendamento,
            Paciente paciente,
            Nutricionista nutricionista,
            LocalDateTime dataHoraInicio,
            LocalDateTime dataHoraFim,
            String tipoConsulta,
            String observacoes
    ) {
        agendamento.setPaciente(paciente);
        agendamento.setNutricionista(nutricionista);
        agendamento.setDataHoraInicio(dataHoraInicio);
        agendamento.setDataHoraFim(dataHoraFim);
        agendamento.setTipoConsulta(tipoConsulta.trim());
        agendamento.setObservacoes(normalizarTexto(observacoes));
    }

    private void validarPeriodo(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
        if (dataHoraInicio == null || dataHoraFim == null) {
            throw new BusinessException("A data e hora de início e fim são obrigatórias.");
        }

        if (!dataHoraFim.isAfter(dataHoraInicio)) {
            throw new BusinessException("A data e hora de fim deve ser posterior à data e hora de início.");
        }
    }

    private void validarPeriodoConsulta(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new BusinessException("O período informado é obrigatório.");
        }

        if (!fim.isAfter(inicio)) {
            throw new BusinessException("A data/hora final do período deve ser posterior à data/hora inicial.");
        }
    }

    private void validarConflitoHorario(
            Long nutricionistaId,
            LocalDateTime dataHoraInicio,
            LocalDateTime dataHoraFim,
            Long agendamentoIdIgnorado
    ) {
        boolean existeConflito = agendamentoIdIgnorado == null
                ? agendamentoRepository.existsByNutricionistaIdAndAtivoTrueAndStatusInAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                nutricionistaId,
                STATUS_QUE_BLOQUEIAM_AGENDA,
                dataHoraFim,
                dataHoraInicio
        )
                : agendamentoRepository.existsByNutricionistaIdAndAtivoTrueAndStatusInAndDataHoraInicioLessThanAndDataHoraFimGreaterThanAndIdNot(
                nutricionistaId,
                STATUS_QUE_BLOQUEIAM_AGENDA,
                dataHoraFim,
                dataHoraInicio,
                agendamentoIdIgnorado
        );

        if (existeConflito) {
            throw new BusinessException("Já existe outro agendamento para esse horário na agenda do nutricionista.");
        }
    }

    private void validarAgendamentoEditavel(Agendamento agendamento) {
        if (StatusAgendamento.CANCELADO.equals(agendamento.getStatus())) {
            throw new BusinessException("Não é possível editar um agendamento cancelado. Crie um novo agendamento.");
        }
    }

    private void validarTransicaoStatus(Agendamento agendamento, StatusAgendamento novoStatus) {
        StatusAgendamento statusAtual = agendamento.getStatus();

        if (statusAtual == novoStatus) {
            return;
        }

        if (StatusAgendamento.CANCELADO.equals(statusAtual)
                && EnumSet.of(StatusAgendamento.REALIZADO, StatusAgendamento.NAO_COMPARECEU).contains(novoStatus)) {
            throw new BusinessException("Não é possível marcar como realizado ou não compareceu um agendamento cancelado.");
        }

        if (StatusAgendamento.REALIZADO.equals(statusAtual)
                && !StatusAgendamento.REALIZADO.equals(novoStatus)) {
            throw new BusinessException("Não é possível alterar o status de um agendamento já realizado.");
        }
    }

    private Paciente buscarPacienteAtivoDoNutricionista(Long pacienteId, Long nutricionistaId) {
        return pacienteRepository.findByIdAndNutricionistaIdAndAtivoTrue(pacienteId, nutricionistaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente não encontrado para o nutricionista autenticado."
                ));
    }

    private Agendamento buscarAgendamentoAtivoDoPaciente(Long agendamentoId, Long pacienteId, Long nutricionistaId) {
        return agendamentoRepository.findByIdAndPacienteIdAndNutricionistaIdAndAtivoTrue(
                        agendamentoId,
                        pacienteId,
                        nutricionistaId
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Agendamento não encontrado para o paciente informado."
                ));
    }

    private String normalizarTexto(String valor) {
        if (!StringUtils.hasText(valor)) {
            return null;
        }
        return valor.trim();
    }

    private Nutricionista getNutricionistaAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof SistemaUserDetails userDetails)) {
            throw new BusinessException("Usuário autenticado inválido.", HttpStatus.UNAUTHORIZED);
        }

        return userDetails.getNutricionista();
    }

    private AgendamentoResponse toResponse(Agendamento agendamento) {
        return new AgendamentoResponse(
                agendamento.getId(),
                agendamento.getPaciente().getId(),
                agendamento.getPaciente().getNome(),
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFim(),
                agendamento.getStatus(),
                agendamento.getTipoConsulta(),
                agendamento.getObservacoes(),
                agendamento.getMotivoCancelamento(),
                agendamento.getRealizadoEm(),
                agendamento.getAtivo(),
                agendamento.getCreatedAt(),
                agendamento.getUpdatedAt()
        );
    }
}