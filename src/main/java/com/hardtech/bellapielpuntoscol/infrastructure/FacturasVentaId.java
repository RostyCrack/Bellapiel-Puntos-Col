package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class FacturasVentaId implements Serializable {
    private String numSerie;
    private int numFactura;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacturasVentaId)) return false;
        FacturasVentaId that = (FacturasVentaId) o;
        return numFactura == that.numFactura &&
                Objects.equals(numSerie, that.numSerie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numSerie, numFactura);
    }
}
