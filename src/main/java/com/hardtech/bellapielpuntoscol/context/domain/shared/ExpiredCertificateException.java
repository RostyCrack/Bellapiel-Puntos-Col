package com.hardtech.bellapielpuntoscol.context.domain.shared;

public class ExpiredCertificateException extends RuntimeException{
    public ExpiredCertificateException(String expiryDate) {
        super("El certificado venció el " + expiryDate + ".");
    }
}
