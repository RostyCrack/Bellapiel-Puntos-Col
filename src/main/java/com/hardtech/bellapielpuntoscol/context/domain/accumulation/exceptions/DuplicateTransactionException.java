package com.hardtech.bellapielpuntoscol.context.domain.accumulation.exceptions;

public class DuplicateTransactionException extends RuntimeException{
    public DuplicateTransactionException() {
        super("La transaccion ya fue procesada anteriormente");
    }
}
