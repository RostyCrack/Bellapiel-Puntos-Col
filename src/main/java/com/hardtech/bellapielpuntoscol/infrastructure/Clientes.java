//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(
        name = "CLIENTES"
)
@Getter
@Setter
public class Clientes {
    @Id
    @Column(
            name = "CODCLIENTE"
    )
    private int codCliente;
    @Column(
            name = "CIF"
    )
    private String documentNo;

    @Column(name = "TELEFONO1")
    private String telefono;

    public Clientes() {
    }

}
