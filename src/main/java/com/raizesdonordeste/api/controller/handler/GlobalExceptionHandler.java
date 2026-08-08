package com.raizesdonordeste.api.controller.handler;

import com.raizesdonordeste.api.domain.dto.ErroResponseDTO;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.exception.RegraNegocioException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Trata erros de recurso não encontrado (HTTP 404)
    // Incluímos também EntityNotFoundException e NoSuchElementException para evitar 500 desnecessário
    @ExceptionHandler({
            RecursoNaoEncontradoException.class,
            EntityNotFoundException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<ErroResponseDTO> handleRecursoNaoEncontrado(
            Exception ex, HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Recurso Não Encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 2. Trata erros de regras de negócio e estoque (HTTP 400 Bad Request)
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponseDTO> handleRegraNegocio(
            RegraNegocioException ex, HttpServletRequest request) {

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Violação de Regra de Negócio",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 3. Trata erros de validação do DTO (@NotNull, @Positive, @Valid, etc.) (HTTP 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidacaoCampos(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errosValidacao = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errosValidacao.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação nos Campos",
                "Um ou mais campos estão inválidos ou ausentes.",
                request.getRequestURI(),
                errosValidacao
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 4. Trata erros genéricos não esperados (HTTP 500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleErroGenerico(
            Exception ex, HttpServletRequest request) {

        // Imprime a pilha do erro no terminal para ajudar no desenvolvimento
        log.error("Erro não tratado detectado na requisição {}: ", request.getRequestURI(), ex);

        ErroResponseDTO erro = new ErroResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                "Ocorreu um erro inesperado no sistema. Contate o administrador.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
