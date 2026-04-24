package br.com.nutricionista.system.controller;

import br.com.nutricionista.system.dto.ApiResponse;
import br.com.nutricionista.system.dto.RedefinirSenhaRequest;
import br.com.nutricionista.system.dto.SolicitarRedefinicaoSenhaRequest;
import br.com.nutricionista.system.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/esqueci-senha")
    public ResponseEntity<ApiResponse<Void>> solicitarRedefinicaoSenha(
            @Valid @RequestBody SolicitarRedefinicaoSenhaRequest request
    ) {
        passwordResetService.solicitarRedefinicaoSenha(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Se houver uma conta vinculada ao e-mail informado, você receberá instruções para redefinir a senha.",
                        null
                )
        );
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<ApiResponse<Void>> redefinirSenha(
            @Valid @RequestBody RedefinirSenhaRequest request
    ) {
        passwordResetService.redefinirSenha(request);

        return ResponseEntity.ok(
                ApiResponse.success("Senha redefinida com sucesso.", null)
        );
    }
}