package com.hlavalle.purchase_transaction.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class CustomExceptionHandler {

    @ExceptionHandler({
            TransactionNotFoundException.class,
            CurrencyConversionUnavailableException.class
    })
    public ResponseEntity<Map<String, List<String>>> handleNotFound(Exception e) {
        log.error(e.getMessage());
        return buildResponseEntity(Collections.singletonList(e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<Map<String, List<String>>> handleBadRequest(MethodArgumentNotValidException e) {

        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        log.error(errors);
        return buildResponseEntity(errors,HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ProblemDetail handleInvalidInputException(MethodArgumentNotValidException e, WebRequest request) {
//        ProblemDetail problemDetail
//                = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
//        problemDetail.setInstance(URI.create("discount"));
//        return problemDetail;
//    }

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
