package com.hardtech.bellapielpuntoscol.infrastructure;


import java.io.Serializable;
import java.util.Objects;


public class PedVentaCabId implements Serializable {
    private String numSerie;
    private int numPedido;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PedVentaCabId)) return false;
        PedVentaCabId that = (PedVentaCabId) o;
        return numPedido == that.numPedido &&
                numSerie.equals(that.numSerie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numSerie, numPedido);
    }

}
