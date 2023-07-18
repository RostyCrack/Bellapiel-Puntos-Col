//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(
        name = "FACTURASVENTASERIESRESOL"
)
public class FacturasVentasSeriesOL {
    @Column(
            name = "NUMEROFISCAL"
    )
    private String numeroFiscal;
    @Column(
            name = "NUMSERIE"
    )
    private String numSerie;
    @Id
    @Column(
            name = "NUMFACTURA"
    )
    private int numFactura;

    public FacturasVentasSeriesOL() {
    }

    public String getNumeroFiscal() {
        return this.numeroFiscal;
    }

    public String getNumSerie() {
        return this.numSerie;
    }

    public int getNumFactura() {
        return this.numFactura;
    }

    public void setNumeroFiscal(final String numeroFiscal) {
        this.numeroFiscal = numeroFiscal;
    }

    public void setNumSerie(final String numSerie) {
        this.numSerie = numSerie;
    }

    public void setNumFactura(final int numFactura) {
        this.numFactura = numFactura;
    }
}
