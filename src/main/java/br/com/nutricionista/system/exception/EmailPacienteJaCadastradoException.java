package br.com.nutricionista.system.exception;

import org.springframework.http.HttpStatus;

public class EmailPacienteJaCadastradoException extends BusinessException {

    public EmailPacienteJaCadastradoException() {
        super(
                "Já existe um paciente ativo cadastrado com este email para o nutricionista autenticado.",
                HttpStatus.CONFLICT
        );
    }
}