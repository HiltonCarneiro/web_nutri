package br.com.nutricionista.system.controller;

import br.com.nutricionista.system.dto.ApiResponse;
import br.com.nutricionista.system.dto.dashboard.DashboardResponse;
import br.com.nutricionista.system.service.DashboardService;
import br.com.nutricionista.system.util.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping(ApiPaths.DASHBOARD)
    public ResponseEntity<ApiResponse<DashboardResponse>> obterDashboard() {
        DashboardResponse response = dashboardService.obterDashboard();
        return ResponseEntity.ok(ApiResponse.success("Dashboard carregado com sucesso.", response));
    }
}