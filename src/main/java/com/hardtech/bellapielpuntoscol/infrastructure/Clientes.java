//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(
        name = "CLIENTES"
)
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

    public Clientes() {
    }

    public int getCodCliente() {
        return this.codCliente;
    }

    public String getDocumentNo() {
        return this.documentNo;
    }

    public void setCodCliente(final int codCliente) {
        this.codCliente = codCliente;
    }

    public void setDocumentNo(final String documentNo) {
        this.documentNo = documentNo;
    }
}
