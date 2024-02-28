package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class TesoreriaId implements Serializable {
    private String serie;
    private int numero;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TesoreriaId)) return false;
        TesoreriaId that = (TesoreriaId) o;
        return numero == that.numero &&
                Objects.equals(serie, that.serie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serie, numero);
    }


}
