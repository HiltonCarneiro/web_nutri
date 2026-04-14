package br.com.nutricionista.system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebNutriApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebNutriApplication.class, args);
    }

    @Bean
    public CommandLineRunner exibirMensagemInicial() {
        return args -> {
            System.out.println("==============================================");
            System.out.println("WEB NUTRI API INICIADA COM SUCESSO");
            System.out.println("Health check: http://localhost:8080/api/v1/health");
            System.out.println("Cadastro: POST http://localhost:8080/auth/cadastro");
            System.out.println("Login:    POST http://localhost:8080/auth/login");
            System.out.println("Logout:   POST http://localhost:8080/auth/logout");
            System.out.println("==============================================");
        };
    }
}