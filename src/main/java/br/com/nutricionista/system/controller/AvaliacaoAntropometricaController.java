package br.com.nutricionista.system.controller;

import br.com.nutricionista.system.dto.ApiResponse;
import br.com.nutricionista.system.dto.prontuario.AtualizacaoAvaliacaoAntropometricaRequest;
import br.com.nutricionista.system.dto.prontuario.AvaliacaoAntropometricaResponse;
import br.com.nutricionista.system.dto.prontuario.CadastroAvaliacaoAntropometricaRequest;
import br.com.nutricionista.system.dto.prontuario.HistoricoAntropometricoResponse;
import br.com.nutricionista.system.service.AvaliacaoAntropometricaService;
import br.com.nutricionista.system.util.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AvaliacaoAntropometricaController {

    private final AvaliacaoAntropometricaService avaliacaoAntropometricaService;

    @PostMapping(ApiPaths.PACIENTE_AVALIACOES_ANTROPOMETRICAS)
    public ResponseEntity<ApiResponse<AvaliacaoAntropometricaResponse>> cadastrar(
            @PathVariable Long pacienteId,
            @Valid @RequestBody CadastroAvaliacaoAntropometricaRequest request
    ) {
        AvaliacaoAntropometricaResponse response = avaliacaoAntropometricaService.cadastrar(pacienteId, request);
        return ResponseEntity.ok(ApiResponse.success("Avaliação antropométrica cadastrada com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_AVALIACOES_ANTROPOMETRICAS)
    public ResponseEntity<ApiResponse<List<AvaliacaoAntropometricaResponse>>> listarPorPaciente(
            @PathVariable Long pacienteId
    ) {
        List<AvaliacaoAntropometricaResponse> response = avaliacaoAntropometricaService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(ApiResponse.success("Avaliações antropométricas listadas com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_AVALIACAO_ANTROPOMETRICA_POR_ID)
    public ResponseEntity<ApiResponse<AvaliacaoAntropometricaResponse>> buscarPorId(
            @PathVariable Long pacienteId,
            @PathVariable Long avaliacaoId
    ) {
        AvaliacaoAntropometricaResponse response = avaliacaoAntropometricaService.buscarPorId(pacienteId, avaliacaoId);
        return ResponseEntity.ok(ApiResponse.success("Avaliação antropométrica encontrada com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_HISTORICO_ANTROPOMETRICO)
    public ResponseEntity<ApiResponse<List<HistoricoAntropometricoResponse>>> obterHistoricoAntropometrico(
            @PathVariable Long pacienteId
    ) {
        List<HistoricoAntropometricoResponse> response = avaliacaoAntropometricaService.obterHistoricoAntropometrico(pacienteId);
        return ResponseEntity.ok(ApiResponse.success("Histórico antropométrico carregado com sucesso.", response));
    }

    @PutMapping(ApiPaths.PACIENTE_AVALIACAO_ANTROPOMETRICA_POR_ID)
    public ResponseEntity<ApiResponse<AvaliacaoAntropometricaResponse>> editar(
            @PathVariable Long pacienteId,
            @PathVariable Long avaliacaoId,
            @Valid @RequestBody AtualizacaoAvaliacaoAntropometricaRequest request
    ) {
        AvaliacaoAntropometricaResponse response = avaliacaoAntropometricaService.editar(
                pacienteId,
                avaliacaoId,
                request
        );
        return ResponseEntity.ok(ApiResponse.success("Avaliação antropométrica atualizada com sucesso.", response));
    }
}