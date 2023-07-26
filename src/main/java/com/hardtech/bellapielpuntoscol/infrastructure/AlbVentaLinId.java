//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class AlbVentaLinId implements Serializable {
    private int numAlbaran;
    private String numSerie;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlbVentaLinId)) return false;
        AlbVentaLinId that = (AlbVentaLinId) o;
        return numAlbaran == that.numAlbaran &&
                Objects.equals(numSerie, that.numSerie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numAlbaran, numSerie);
    }

}
