package com.hardtech.bellapielpuntoscol.context.domain.paymentLink.exceptions;

public class PedidoNotFoundException extends RuntimeException{
    public PedidoNotFoundException() {
        super("Pedido no encontrado");
    }
}
