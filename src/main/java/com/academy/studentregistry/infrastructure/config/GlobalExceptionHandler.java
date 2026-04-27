package com.academy.studentregistry.infrastructure.config;

import com.academy.studentregistry.domain.exception.StudentAlreadyExistsException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja el caso de ID duplicado → HTTP 409 Conflict
     */
    @ExceptionHandler(StudentAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateId(StudentAlreadyExistsException ex) {
        return Map.of("error", ex.getMessage());
    }

    /**
     * Maneja errores de validacion de campos → HTTP 400 Bad Request
     */
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationErrors(WebExchangeBindException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage()
                ));
        return Map.of("errors", fieldErrors);
    }

    @ExceptionHandler(DecodingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDecodingException(DecodingException ex) {
        if (ex.getCause() instanceof UnrecognizedPropertyException upe) {
            return Map.of(
                    "error", "Campo no permitido: '" + upe.getPropertyName() + "'"
            );
        }
        return Map.of("error", "Error al procesar el cuerpo de la peticion");
    }
}
