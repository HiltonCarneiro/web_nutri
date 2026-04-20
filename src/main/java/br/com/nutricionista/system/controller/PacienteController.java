package br.com.nutricionista.system.controller;

import br.com.nutricionista.system.dto.ApiResponse;
import br.com.nutricionista.system.dto.AtualizacaoPacienteRequest;
import br.com.nutricionista.system.dto.CadastroPacienteRequest;
import br.com.nutricionista.system.dto.PacienteResponse;
import br.com.nutricionista.system.service.PacienteService;
import br.com.nutricionista.system.util.ApiPaths;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.PACIENTES)
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<ApiResponse<PacienteResponse>> cadastrar(
            @Valid @RequestBody CadastroPacienteRequest request
    ) {
        PacienteResponse response = pacienteService.cadastrar(request);
        return ResponseEntity.ok(ApiResponse.success("Paciente cadastrado com sucesso.", response));
    }

    @PutMapping("/{pacienteId}")
    public ResponseEntity<ApiResponse<PacienteResponse>> editar(
            @PathVariable Long pacienteId,
            @Valid @RequestBody AtualizacaoPacienteRequest request
    ) {
        PacienteResponse response = pacienteService.editar(pacienteId, request);
        return ResponseEntity.ok(ApiResponse.success("Paciente atualizado com sucesso.", response));
    }

    @DeleteMapping("/{pacienteId}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Long pacienteId) {
        pacienteService.excluir(pacienteId);
        return ResponseEntity.ok(ApiResponse.success("Paciente excluído com sucesso.", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PacienteResponse>>> listar() {
        List<PacienteResponse> response = pacienteService.listarAtivos();
        return ResponseEntity.ok(ApiResponse.success("Pacientes listados com sucesso.", response));
    }

    @GetMapping("/busca")
    public ResponseEntity<ApiResponse<List<PacienteResponse>>> buscarPorNome(
            @RequestParam(required = false) String nome
    ) {
        List<PacienteResponse> response = pacienteService.buscarPorNome(nome);
        return ResponseEntity.ok(ApiResponse.success("Busca de pacientes realizada com sucesso.", response));
    }

    @GetMapping("/{pacienteId}")
    public ResponseEntity<ApiResponse<PacienteResponse>> buscarPorId(@PathVariable Long pacienteId) {
        PacienteResponse response = pacienteService.buscarPorId(pacienteId);
        return ResponseEntity.ok(ApiResponse.success("Paciente encontrado com sucesso.", response));
    }
}