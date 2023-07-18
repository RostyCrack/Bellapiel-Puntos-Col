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
        name = "ALBVENTALIN"
)
@IdClass(AlbVentaLinId.class)
public class AlbVentaLin {
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
            name = "SUPEDIDO"
    )
    private String numSerieFactura;

    public AlbVentaLin() {
    }

    public String getNumSerie() {
        return this.numSerie;
    }

    public int getNumAlbaran() {
        return this.numAlbaran;
    }

    public String getNumSerieFactura() {
        return this.numSerieFactura;
    }

    public void setNumSerie(final String numSerie) {
        this.numSerie = numSerie;
    }

    public void setNumAlbaran(final int numAlbaran) {
        this.numAlbaran = numAlbaran;
    }

    public void setNumSerieFactura(final String numSerieFactura) {
        this.numSerieFactura = numSerieFactura;
    }
}
