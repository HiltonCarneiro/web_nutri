package br.com.nutricionista.system.exception;

import org.springframework.http.HttpStatus;

public class CredenciaisInvalidasException extends BusinessException {

    public CredenciaisInvalidasException() {
        super("Email ou senha inválidos.", HttpStatus.UNAUTHORIZED);
    }
}