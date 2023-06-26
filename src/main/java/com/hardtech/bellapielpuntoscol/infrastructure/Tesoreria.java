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
        name = "TESORERIA"
)
@IdClass(TesoreriaId.class)
public class Tesoreria {
    @Id
    @Column(
            name = "SERIE"
    )
    private String serie;
    @Id
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
