package com.hardtech.bellapielpuntoscol.context.domain.accumulation.exceptions;

import com.hardtech.bellapielpuntoscol.infrastructure.PaymentMethod;

public class PaymentMethodDontAccumulateException extends RuntimeException {
    public PaymentMethodDontAccumulateException() {
        super("El método de pago no acumula");
    }

}
