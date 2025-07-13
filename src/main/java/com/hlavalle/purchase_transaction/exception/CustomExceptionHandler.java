package com.hlavalle.purchase_transaction.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class CustomExceptionHandler {

    @ExceptionHandler({
            TransactionNotFoundException.class
    })
    public ResponseEntity<Map<String, List<String>>> handleTransactionNotFound(Exception e) {
        log.error(e.getMessage());
        return buildResponseEntity(Collections.singletonList(e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            CurrencyConversionException.class,
            RuntimeException.class,
            Exception.class,
    })
    public ResponseEntity<Map<String, List<String>>> handleInternalServerError(Exception e) {
        log.error(e.getMessage());
        return buildResponseEntity(Collections.singletonList("Internal server error."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String,List<String>>> buildResponseEntity(List<String> errors, HttpStatus status) {
        Map<String, List<String>> errorResponse = Map.of("errors", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), status);
    }
}
