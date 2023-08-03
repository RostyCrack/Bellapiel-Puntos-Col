//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "CLIENTES")
public class Clientes {
    @Id
    @Column(name = "CODCLIENTE")
    private int codCliente;
    @Column(name = "CIF")
    private String documentNo;
    @Column(name = "TELEFONO1")
    private String telefono;

    public Clientes() {
    }

}
