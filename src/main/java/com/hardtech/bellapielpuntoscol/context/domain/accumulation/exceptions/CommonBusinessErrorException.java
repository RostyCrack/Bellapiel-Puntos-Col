package com.hardtech.bellapielpuntoscol.context.domain.accumulation.exceptions;

public class CommonBusinessErrorException extends RuntimeException{
    public CommonBusinessErrorException() {
        super("No se puede acumular mas de tres veces en un dia");
    }
}
