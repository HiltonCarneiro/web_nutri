package br.com.nutricionista.system.controller;

import br.com.nutricionista.system.dto.ApiResponse;
import br.com.nutricionista.system.dto.AtualizacaoDietaRequest;
import br.com.nutricionista.system.dto.AtualizacaoItemRefeicaoRequest;
import br.com.nutricionista.system.dto.AtualizacaoRefeicaoRequest;
import br.com.nutricionista.system.dto.CadastroDietaRequest;
import br.com.nutricionista.system.dto.CadastroItemRefeicaoRequest;
import br.com.nutricionista.system.dto.CadastroRefeicaoRequest;
import br.com.nutricionista.system.dto.DietaResponse;
import br.com.nutricionista.system.dto.DietaResumoResponse;
import br.com.nutricionista.system.dto.HistoricoDietaResponse;
import br.com.nutricionista.system.dto.ItemRefeicaoResponse;
import br.com.nutricionista.system.dto.RefeicaoResponse;
import br.com.nutricionista.system.service.DietaService;
import br.com.nutricionista.system.util.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DietaController {

    private final DietaService dietaService;

    @PostMapping(ApiPaths.PACIENTE_DIETAS)
    public ResponseEntity<ApiResponse<DietaResponse>> cadastrarDieta(
            @PathVariable Long pacienteId,
            @Valid @RequestBody CadastroDietaRequest request
    ) {
        DietaResponse response = dietaService.cadastrarDieta(pacienteId, request);
        return ResponseEntity.ok(ApiResponse.success("Dieta cadastrada com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_DIETAS)
    public ResponseEntity<ApiResponse<List<DietaResumoResponse>>> listarDietas(
            @PathVariable Long pacienteId
    ) {
        List<DietaResumoResponse> response = dietaService.listarPorPaciente(pacienteId);
        return ResponseEntity.ok(ApiResponse.success("Dietas listadas com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_HISTORICO_DIETAS)
    public ResponseEntity<ApiResponse<List<HistoricoDietaResponse>>> obterHistorico(
            @PathVariable Long pacienteId
    ) {
        List<HistoricoDietaResponse> response = dietaService.obterHistorico(pacienteId);
        return ResponseEntity.ok(ApiResponse.success("Histórico de dietas carregado com sucesso.", response));
    }

    @GetMapping(ApiPaths.PACIENTE_DIETA_POR_ID)
    public ResponseEntity<ApiResponse<DietaResponse>> buscarDieta(
            @PathVariable Long pacienteId,
            @PathVariable Long dietaId
    ) {
        DietaResponse response = dietaService.buscarDieta(pacienteId, dietaId);
        return ResponseEntity.ok(ApiResponse.success("Dieta encontrada com sucesso.", response));
    }

    @PutMapping(ApiPaths.PACIENTE_DIETA_POR_ID)
    public ResponseEntity<ApiResponse<DietaResponse>> editarDieta(
            @PathVariable Long pacienteId,
            @PathVariable Long dietaId,
            @Valid @RequestBody AtualizacaoDietaRequest request
    ) {
        DietaResponse response = dietaService.editarDieta(pacienteId, dietaId, request);
        return ResponseEntity.ok(ApiResponse.success("Dieta atualizada com sucesso.", response));
    }

    @DeleteMapping(ApiPaths.PACIENTE_DIETA_POR_ID)
    public ResponseEntity<ApiResponse<Void>> excluirDieta(
            @PathVariable Long pacienteId,
            @PathVariable Long dietaId
    ) {
        dietaService.excluirDieta(pacienteId, dietaId);
        return ResponseEntity.ok(ApiResponse.success("Dieta excluída com sucesso.", null));
    }

    @PostMapping(ApiPaths.PACIENTE_DIETA_REFEICOES)
    public ResponseEntity<ApiResponse<RefeicaoResponse>> cadastrarRefeicao(
            @PathVariable Long pacienteId,
            @PathVariable Long dietaId,
            @Valid @RequestBody CadastroRefeicaoRequest request
    ) {
        RefeicaoResponse response = dietaService.cadastrarRefeicao(pacienteId, dietaId, request);
        return ResponseEntity.ok(ApiResponse.success("Refeição cadastrada com sucesso.", response));
    }

    @PutMapping(ApiPaths.PACIENTE_DIETA_REFEICAO_POR_ID)
    public ResponseEntity<ApiResponse<RefeicaoResponse>> editarRefeicao(
            @PathVariable Long pacienteId,
            @PathVariable Long dietaId,
            @PathVariable Long refeicaoId,
            @Valid @RequestBody AtualizacaoRefeicaoRequest request
    ) {
        RefeicaoResponse response = dietaService.editarRefeicao(pacienteId, dietaId, refeicaoId, request);
        return ResponseEntity.ok(ApiResponse.success("Refeição atualizada com sucesso.", response));
    }

    @DeleteMapping(ApiPaths.PACIENTE_DIETA_REFEICAO_POR_ID)
    public ResponseEntity<ApiResponse<Void>> excluirRefeicao(
            @PathVariable Long pacienteId,
            @PathVariable Long dietaId,
            @PathVariable Long refeicaoId
    ) {
        dietaService.excluirRefeicao(pacienteId, dietaId, refeicaoId);
        return ResponseEntity.ok(ApiResponse.success("Refeição excluída com sucesso.", null));
    }

    @PostMapping(ApiPaths.PACIENTE_DIETA_REFEICAO_ITENS)
    public ResponseEntity<ApiResponse<ItemRefeicaoResponse>> cadastrarItem(
            @PathVariable Long pacienteId,
            @PathVariable Long dietaId,
            @PathVariable Long refeicaoId,
            @Valid @RequestBody CadastroItemRefeicaoRequest request
    ) {
        ItemRefeicaoResponse response = dietaService.cadastrarItem(pacienteId, dietaId, refeicaoId, request);
        return ResponseEntity.ok(ApiResponse.success("Item da refeição cadastrado com sucesso.", response));
    }

    @PutMapping(ApiPaths.PACIENTE_DIETA_REFEICAO_ITEM_POR_ID)
    public ResponseEntity<ApiResponse<ItemRefeicaoResponse>> editarItem(
            @PathVariable Long pacienteId,
            @PathVariable Long dietaId,
            @PathVariable Long refeicaoId,
            @PathVariable Long itemId,
            @Valid @RequestBody AtualizacaoItemRefeicaoRequest request
    ) {
        ItemRefeicaoResponse response = dietaService.editarItem(
                pacienteId,
                dietaId,
                refeicaoId,
                itemId,
                request
        );
        return ResponseEntity.ok(ApiResponse.success("Item da refeição atualizado com sucesso.", response));
    }

    @DeleteMapping(ApiPaths.PACIENTE_DIETA_REFEICAO_ITEM_POR_ID)
    public ResponseEntity<ApiResponse<Void>> excluirItem(
            @PathVariable Long pacienteId,
            @PathVariable Long dietaId,
            @PathVariable Long refeicaoId,
            @PathVariable Long itemId
    ) {
        dietaService.excluirItem(pacienteId, dietaId, refeicaoId, itemId);
        return ResponseEntity.ok(ApiResponse.success("Item da refeição excluído com sucesso.", null));
    }
}