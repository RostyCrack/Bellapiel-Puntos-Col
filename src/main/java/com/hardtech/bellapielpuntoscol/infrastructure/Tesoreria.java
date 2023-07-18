//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(
        name = "TESORERIA"
)

public class Tesoreria {

    @Id
    @Column(
            name = "POSICION"
    )
    private String posicion;

    @Column(
            name = "SERIE"
    )
    private String serie;

    @Column(
            name = "NUMERO"
    )
    private int numero;
    @Column(
            name = "CODTIPOPAGO"
    )
    private String codTipoPago;
    @Column(
            name = "IMPORTE"
    )
    private int importe;

    public Tesoreria() {
    }

    public String getSerie() {
        return this.serie;
    }

    public int getNumero() {
        return this.numero;
    }

    public String getCodTipoPago() {
        return this.codTipoPago;
    }

    public int getImporte() {
        return this.importe;
    }

    public void setSerie(final String serie) {
        this.serie = serie;
    }

    public void setNumero(final int numero) {
        this.numero = numero;
    }

    public void setCodTipoPago(final String codTipoPago) {
        this.codTipoPago = codTipoPago;
    }

    public void setImporte(final int importe) {
        this.importe = importe;
    }
}
