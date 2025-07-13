package com.hlavalle.purchase_transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CurrencyConversionUnavailableException extends RuntimeException {
    String message;

    public CurrencyConversionUnavailableException(String message) {
        super(message);
        this.message = message;
    }
}
