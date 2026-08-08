package com.raizesdonordeste.api.domain.exception;

public class RegraNegocioException extends RuntimeException {

    // Construtor que recebe a mensagem de erro
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
