package br.com.nutricionista.system.controller;

import br.com.nutricionista.system.dto.ApiResponse;
import br.com.nutricionista.system.dto.CadastroNutricionistaRequest;
import br.com.nutricionista.system.dto.LoginRequest;
import br.com.nutricionista.system.dto.LoginResponse;
import br.com.nutricionista.system.dto.NutricionistaResponse;
import br.com.nutricionista.system.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/cadastro")
    public ResponseEntity<ApiResponse<NutricionistaResponse>> cadastrar(
            @Valid @RequestBody CadastroNutricionistaRequest request
    ) {
        NutricionistaResponse response = authService.cadastrar(request);
        return ResponseEntity.ok(ApiResponse.success("Nutricionista cadastrado com sucesso.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        LoginResponse response = authService.login(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success("Login realizado com sucesso.", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authService.logout(request, response);
        return ResponseEntity.ok(ApiResponse.success("Logout realizado com sucesso.", null));
    }
}