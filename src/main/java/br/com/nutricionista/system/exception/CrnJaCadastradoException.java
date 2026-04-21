package br.com.nutricionista.system.exception;

import org.springframework.http.HttpStatus;

public class CrnJaCadastradoException extends BusinessException {

    public CrnJaCadastradoException() {
        super("Já existe um nutricionista cadastrado com este CRN.", HttpStatus.CONFLICT);
    }
}