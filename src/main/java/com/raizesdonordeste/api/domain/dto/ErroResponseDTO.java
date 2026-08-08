package com.raizesdonordeste.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResponseDTO(
        LocalDateTime timestamp,
        Integer status,
        String erro,
        String mensagem,
        String path,
        Map<String, String> validacoes
) {
    // Construtor conveniente 1: Para erros sem validação de campos (ex: 404, 500)
    public ErroResponseDTO(Integer status, String erro, String mensagem, String path) {
        this(LocalDateTime.now(), status, erro, mensagem, path, null);
    }

    // Construtor conveniente 2: Para erros de validação com o mapa de campos (ex: 400)
    public ErroResponseDTO(Integer status, String erro, String mensagem, String path, Map<String, String> validacoes) {
        this(LocalDateTime.now(), status, erro, mensagem, path, validacoes);
    }
}