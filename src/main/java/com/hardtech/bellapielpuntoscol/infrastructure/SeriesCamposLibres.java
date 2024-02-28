//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity(
        name = "SERIESCAMPOSLIBRES"
)
public class SeriesCamposLibres {
    @Id
    @Column(
            name = "SERIE"
    )
    private String serie;
    @Column(
            name = "CODIGO_ALMACEN"
    )
    private String locationCode;

    public void setSerie(final String serie) {
        this.serie = serie;
    }

    public void setLocationCode(final String locationCode) {
        this.locationCode = locationCode;
    }

    public SeriesCamposLibres() {
    }
}
