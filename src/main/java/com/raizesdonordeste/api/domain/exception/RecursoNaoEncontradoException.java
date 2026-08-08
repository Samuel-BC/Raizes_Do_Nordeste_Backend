package com.raizesdonordeste.api.domain.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    // Construtor que recebe a mensagem de erro personalizada
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
