package br.com.nutricionista.system.exception;

import org.springframework.http.HttpStatus;

public class EmailJaCadastradoException extends BusinessException {

    public EmailJaCadastradoException() {
        super("Já existe um nutricionista cadastrado com este email.", HttpStatus.CONFLICT);
    }
}