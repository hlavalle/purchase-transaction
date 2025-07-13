package com.hlavalle.purchase_transaction.exception;

public class TransactionNotFoundException extends RuntimeException {

    String message;

    public TransactionNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
