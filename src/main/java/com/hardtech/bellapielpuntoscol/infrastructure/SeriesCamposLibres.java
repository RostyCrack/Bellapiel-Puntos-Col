//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
            name = "LOCATION_CODE"
    )
    private String locationCode;

    public String getSerie() {
        return this.serie;
    }

    public String getLocationCode() {
        return this.locationCode;
    }

    public void setSerie(final String serie) {
        this.serie = serie;
    }

    public void setLocationCode(final String locationCode) {
        this.locationCode = locationCode;
    }

    public SeriesCamposLibres() {
    }
}
