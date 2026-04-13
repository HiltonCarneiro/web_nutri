package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class HealthCheckService {

    @Value("${spring.application.name}")
    private String applicationName;

    public ApiResponse<Map<String, Object>> healthCheck() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("application", applicationName);
        data.put("status", "UP");
        data.put("layer", "backend");
        data.put("database", "PostgreSQL / Supabase ready");

        return ApiResponse.success("Aplicação em execução.", data);
    }
}