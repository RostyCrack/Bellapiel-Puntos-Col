package com.hardtech.bellapielpuntoscol.infrastructure;

import javax.persistence.IdClass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FacturasVentaId implements Serializable {
    private String numSerie;
    private int numFactura;

}
