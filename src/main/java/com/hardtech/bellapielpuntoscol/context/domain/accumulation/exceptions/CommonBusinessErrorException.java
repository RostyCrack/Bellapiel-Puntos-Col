package com.hardtech.bellapielpuntoscol.context.domain.accumulation.exceptions;

public class CommonBusinessErrorException extends RuntimeException{
    public CommonBusinessErrorException() {
        super("Ha alcanzado el limite de acumulaciones permitidas en un dia");
    }
}
