package br.com.nutricionista.system.controller;

import br.com.nutricionista.system.dto.AgendamentoResponse;
import br.com.nutricionista.system.dto.ApiResponse;
import br.com.nutricionista.system.dto.AtualizacaoAgendamentoRequest;
import br.com.nutricionista.system.dto.AtualizacaoStatusAgendamentoRequest;
import br.com.nutricionista.system.dto.CadastroAgendamentoRequest;
import br.com.nutricionista.system.dto.CancelamentoAgendamentoRequest;
import br.com.nutricionista.system.service.AgendamentoService;
import br.com.nutricionista.system.util.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping(ApiPaths.PACIENTE_AGENDAMENTOS)
    public ResponseEntity<ApiResponse<AgendamentoResponse>> cadastrar(
            @PathVariable Long pacienteId,
            @Valid @RequestBody CadastroAgendamentoRequest request
    ) {
        AgendamentoResponse response = agendamentoService.cadastrar(pacienteId, request);
        return ResponseEntity.ok(ApiResponse.success("Agendamento cadastrado com sucesso.", response));
    }

    @PutMapping(ApiPaths.PACIENTE_AGENDAMENTO_POR_ID)
    public ResponseEntity<ApiResponse<AgendamentoResponse>> editar(
            @PathVariable Long pacienteId,
            @PathVariable Long agendamentoId,
            @Valid @RequestBody AtualizacaoAgendamentoRequest request
    ) {
        AgendamentoResponse response = agendamentoService.editar(pacienteId, agendamentoId, request);
        return ResponseEntity.ok(ApiResponse.success("Agendamento atualizado com sucesso.", response));
    }

    @PatchMapping(ApiPaths.PACIENTE_AGENDAMENTO_CANCELAMENTO)
    public ResponseEntity<ApiResponse<AgendamentoResponse>> cancelar(
            @PathVariable Long pacienteId,
            @PathVariable Long agendamentoId,
            @Valid @RequestBody CancelamentoAgendamentoRequest request
    ) {
        AgendamentoResponse response = agendamentoService.cancelar(pacienteId, agendamentoId, request);
        return ResponseEntity.ok(ApiResponse.success("Agendamento cancelado com sucesso.", response));
    }

    @PatchMapping(ApiPaths.PACIENTE_AGENDAMENTO_STATUS)
    public ResponseEntity<ApiResponse<AgendamentoResponse>> atualizarStatus(
            @PathVariable Long pacienteId,
            @PathVariable Long agendamentoId,
            @Valid @RequestBody AtualizacaoStatusAgendamentoRequest request
    ) {
        AgendamentoResponse response = agendamentoService.atualizarStatus(pacienteId, agendamentoId, request);
        return ResponseEntity.ok(ApiResponse.success("Status do agendamento atualizado com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_AGENDAMENTOS)
    public ResponseEntity<ApiResponse<List<AgendamentoResponse>>> listarPorPaciente(
            @PathVariable Long pacienteId
    ) {
        List<AgendamentoResponse> response = agendamentoService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(ApiResponse.success("Agendamentos do paciente listados com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_AGENDAMENTO_POR_ID)
    public ResponseEntity<ApiResponse<AgendamentoResponse>> buscarPorId(
            @PathVariable Long pacienteId,
            @PathVariable Long agendamentoId
    ) {
        AgendamentoResponse response = agendamentoService.buscarPorId(pacienteId, agendamentoId);
        return ResponseEntity.ok(ApiResponse.success("Agendamento encontrado com sucesso.", response));
    }

    @GetMapping(ApiPaths.AGENDAMENTOS_DO_DIA)
    public ResponseEntity<ApiResponse<List<AgendamentoResponse>>> listarAgendaDoDia(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data
    ) {
        List<AgendamentoResponse> response = agendamentoService.listarAgendaDoDia(data);
        return ResponseEntity.ok(ApiResponse.success("Agenda do dia carregada com sucesso.", response));
    }

    @GetMapping(ApiPaths.AGENDAMENTOS_POR_PERIODO)
    public ResponseEntity<ApiResponse<List<AgendamentoResponse>>> listarPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime inicio,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fim
    ) {
        List<AgendamentoResponse> response = agendamentoService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(ApiResponse.success("Agendamentos do período carregados com sucesso.", response));
    }
}