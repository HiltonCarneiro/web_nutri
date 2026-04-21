package br.com.nutricionista.system.exception;

import org.springframework.http.HttpStatus;

public class CpfPacienteJaCadastradoException extends BusinessException {

    public CpfPacienteJaCadastradoException() {
        super(
                "Já existe um paciente ativo cadastrado com este CPF para o nutricionista autenticado.",
                HttpStatus.CONFLICT
        );
    }
}