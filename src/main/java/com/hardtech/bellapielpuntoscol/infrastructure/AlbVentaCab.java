//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(
        name = "ALBVENTACAB"
)
@IdClass(AlbVentaLinId.class)
public class AlbVentaCab {
    @Id
    @Column(
            name = "NUMSERIE"
    )
    private String numSerie;
    @Id
    @Column(
            name = "NUMALBARAN"
    )
    private int numAlbaran;
    @Column(
            name = "NUMFAC"
    )
    private int numFactura;

    public AlbVentaCab() {
    }

    public String getNumSerie() {
        return this.numSerie;
    }

    public int getNumAlbaran() {
        return this.numAlbaran;
    }

    public int getNumFactura() {
        return this.numFactura;
    }

    public void setNumSerie(final String numSerie) {
        this.numSerie = numSerie;
    }

    public void setNumAlbaran(final int numAlbaran) {
        this.numAlbaran = numAlbaran;
    }

    public void setNumFactura(final int numFactura) {
        this.numFactura = numFactura;
    }
}
