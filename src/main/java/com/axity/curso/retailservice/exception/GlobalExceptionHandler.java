package com.axity.curso.retailservice.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarRecursoNoEncontrado(
            RecursoNoEncontradoException exception, HttpServletRequest request) {
        return crearError(HttpStatus.NOT_FOUND, "RecursoNoEncontradoException",
                exception.getMessage(), request);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<ApiError> manejarStockInsuficiente(
            StockInsuficienteException exception, HttpServletRequest request) {
        return crearError(HttpStatus.CONFLICT, "StockInsuficienteException",
                exception.getMessage(), request);
    }

    @ExceptionHandler(PrecioInvalidoException.class)
    public ResponseEntity<ApiError> manejarPrecioInvalido(
            PrecioInvalidoException exception, HttpServletRequest request) {
        return crearError(HttpStatus.BAD_REQUEST, "PrecioInvalidoException",
                exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        boolean precioInvalido = exception.getBindingResult().getFieldErrors().stream()
                .anyMatch(error -> error.getField().equals("precio")
                        && error.getCode() != null
                        && error.getCode().equals("Positive"));
        String mensaje = exception.getBindingResult().getFieldErrors().stream()
                .map(this::mensajeValidacion)
                .collect(Collectors.joining("; "));
        String tipoError = precioInvalido ? "PrecioInvalidoException" : "SolicitudInvalida";
        if (precioInvalido) {
            mensaje = "El precio debe ser mayor a cero";
        }
        return crearError(HttpStatus.BAD_REQUEST, tipoError, mensaje, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> manejarArgumentoInvalido(
            IllegalArgumentException exception, HttpServletRequest request) {
        return crearError(HttpStatus.BAD_REQUEST, "SolicitudInvalida",
                exception.getMessage(), request);
    }

    private String mensajeValidacion(FieldError error) {
        return error.getDefaultMessage() == null
                ? "Valor inválido para " + error.getField()
                : error.getDefaultMessage();
    }

    private ResponseEntity<ApiError> crearError(
            HttpStatus status, String error, String message, HttpServletRequest request) {
        ApiError body = new ApiError(
                Instant.now(), status.value(), error, message, request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
