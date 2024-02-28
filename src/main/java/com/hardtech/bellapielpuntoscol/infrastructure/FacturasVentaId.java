package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
        return getNumFactura() == that.getNumFactura() && getNumSerie().equals(that.getNumSerie());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getNumSerie(), getNumFactura());
    }

}
