//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(
        name = "CLIENTESCAMPOSLIBRES"
)
public class ClientesCamposLibres {
    @Id
    @Column(
            name = "CODCLIENTE"
    )
    private int codCliente;
    @Column(
            name = "TIPO_DE_DOCUMENTO"
    )
    private String tipoDeDocumento;

    public ClientesCamposLibres() {
    }

    public int getCodCliente() {
        return this.codCliente;
    }

    public String getTipoDeDocumento() {
        return this.tipoDeDocumento;
    }

    public void setCodCliente(final int codCliente) {
        this.codCliente = codCliente;
    }

    public void setTipoDeDocumento(final String tipoDeDocumento) {
        this.tipoDeDocumento = tipoDeDocumento;
    }
}
