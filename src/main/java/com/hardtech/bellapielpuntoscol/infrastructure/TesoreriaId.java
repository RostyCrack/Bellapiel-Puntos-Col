package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TesoreriaId implements Serializable {
    private String serie;
    private int numero;
}
