package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class PedVentaLinId implements Serializable {
    private String numSerie;
    private int numPedido;
    private String codArticulo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PedVentaLinId)) return false;
        PedVentaLinId that = (PedVentaLinId) o;
        return numPedido == that.numPedido &&
                Objects.equals(numSerie, that.numSerie) &&
                Objects.equals(codArticulo, that.codArticulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numSerie, numPedido, codArticulo);
    }
}
