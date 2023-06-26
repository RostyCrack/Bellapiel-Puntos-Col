//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity(
        name = "FACTURASVENTA"
)
@IdClass(FacturasVentaId.class)
public class FacturasVenta {
    @Id
    @Column(
            name = "NUMSERIE"
    )
    private String numSerie;
    @Id
    @Column(
            name = "NUMFACTURA"
    )
    private int numFactura;
    @Column(
            name = "CODCLIENTE"
    )
    private int codCliente;
    @Column(
            name = "CAJA"
    )
    private String caja;
    @Column(
            name = "CODVENDEDOR"
    )
    private String codVendedor;

    public FacturasVenta() {
    }

    public String getNumSerie() {
        return this.numSerie;
    }

    public int getNumFactura() {
        return this.numFactura;
    }

    public int getCodCliente() {
        return this.codCliente;
    }

    public String getCaja() {
        return this.caja;
    }

    public String getCodVendedor() {
        return this.codVendedor;
    }

    public void setNumSerie(final String numSerie) {
        this.numSerie = numSerie;
    }

    public void setNumFactura(final int numFactura) {
        this.numFactura = numFactura;
    }

    public void setCodCliente(final int codCliente) {
        this.codCliente = codCliente;
    }

    public void setCaja(final String caja) {
        this.caja = caja;
    }

    public void setCodVendedor(final String codVendedor) {
        this.codVendedor = codVendedor;
    }
}
