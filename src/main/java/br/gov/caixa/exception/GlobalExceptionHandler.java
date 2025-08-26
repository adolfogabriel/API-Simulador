package br.gov.caixa.exception;

import br.gov.caixa.model.RetornoDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RetornoDto> handleConstraintViolation(ConstraintViolationException ex) {

        return new ResponseEntity<>(RetornoDto.builder()
                .mensagem("Parâmetros informados incorretos")
                .violacoes(ex.getConstraintViolations()
                        .stream()
                        .map(v -> v.getMessage())
                        .toList())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RetornoDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> violacoes = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        return new ResponseEntity<>(RetornoDto.builder()
                .mensagem("Parâmetros formato inválido.")
                .violacoes(violacoes)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RetornoDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(RetornoDto.builder()
                .mensagem("Parâmetros não informados ou com formato inválido.")
                .build(), HttpStatus.BAD_REQUEST);
    }
}