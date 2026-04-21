package br.com.nutricionista.system.controller;

import br.com.nutricionista.system.dto.ApiResponse;
import br.com.nutricionista.system.dto.AtualizacaoEvolucaoRequest;
import br.com.nutricionista.system.dto.CadastroEvolucaoRequest;
import br.com.nutricionista.system.dto.EvolucaoConsultaResponse;
import br.com.nutricionista.system.dto.ProntuarioCompletoResponse;
import br.com.nutricionista.system.service.EvolucaoConsultaService;
import br.com.nutricionista.system.service.ProntuarioCompletoService;
import br.com.nutricionista.system.util.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EvolucaoConsultaController {

    private final EvolucaoConsultaService evolucaoConsultaService;
    private final ProntuarioCompletoService prontuarioCompletoService;

    @PostMapping(ApiPaths.PACIENTE_EVOLUCOES)
    public ResponseEntity<ApiResponse<EvolucaoConsultaResponse>> cadastrar(
            @PathVariable Long pacienteId,
            @Valid @RequestBody CadastroEvolucaoRequest request
    ) {
        EvolucaoConsultaResponse response = evolucaoConsultaService.cadastrar(pacienteId, request);
        return ResponseEntity.ok(ApiResponse.success("Evolução cadastrada com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_EVOLUCOES)
    public ResponseEntity<ApiResponse<List<EvolucaoConsultaResponse>>> listarPorPaciente(
            @PathVariable Long pacienteId
    ) {
        List<EvolucaoConsultaResponse> response = evolucaoConsultaService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(ApiResponse.success("Evoluções listadas com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_EVOLUCAO_POR_ID)
    public ResponseEntity<ApiResponse<EvolucaoConsultaResponse>> buscarPorId(
            @PathVariable Long pacienteId,
            @PathVariable Long evolucaoId
    ) {
        EvolucaoConsultaResponse response = evolucaoConsultaService.buscarPorId(pacienteId, evolucaoId);
        return ResponseEntity.ok(ApiResponse.success("Evolução encontrada com sucesso.", response));
    }

    @PutMapping(ApiPaths.PACIENTE_EVOLUCAO_POR_ID)
    public ResponseEntity<ApiResponse<EvolucaoConsultaResponse>> editar(
            @PathVariable Long pacienteId,
            @PathVariable Long evolucaoId,
            @Valid @RequestBody AtualizacaoEvolucaoRequest request
    ) {
        EvolucaoConsultaResponse response = evolucaoConsultaService.editar(
                pacienteId,
                evolucaoId,
                request
        );
        return ResponseEntity.ok(ApiResponse.success("Evolução atualizada com sucesso.", response));
    }

    @DeleteMapping(ApiPaths.PACIENTE_EVOLUCAO_POR_ID)
    public ResponseEntity<ApiResponse<Void>> excluir(
            @PathVariable Long pacienteId,
            @PathVariable Long evolucaoId
    ) {
        evolucaoConsultaService.excluir(pacienteId, evolucaoId);
        return ResponseEntity.ok(ApiResponse.success("Evolução excluída com sucesso.", null));
    }

    @GetMapping(ApiPaths.PACIENTE_PRONTUARIO)
    public ResponseEntity<ApiResponse<ProntuarioCompletoResponse>> obterProntuarioCompleto(
            @PathVariable Long pacienteId
    ) {
        ProntuarioCompletoResponse response = prontuarioCompletoService.obterProntuarioCompleto(pacienteId);
        return ResponseEntity.ok(ApiResponse.success("Prontuário completo carregado com sucesso.", response));
    }
}