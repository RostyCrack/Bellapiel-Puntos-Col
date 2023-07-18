//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(
        name = "FACTURASVENTA"
)
@IdClass(FacturasVentaId.class)
@Getter
@Setter
@NoArgsConstructor
public class FacturasVenta {
    @Id
    @Column(
            name = "NUMSERIE"
    )
    private String numSerie;
    @Id
    @Column(
            name = "NUMFACTURA"
    )
    private int numFactura;
    @Column(
            name = "CODCLIENTE"
    )
    private int codCliente;
    @Column(
            name = "CAJA"
    )
    private String caja;
    @Column(
            name = "CODVENDEDOR"
    )
    private String codVendedor;
    @Column(
            name = "FECHA"
    )
    private LocalDateTime fecha;

    @Column(
            name = "HORA"
    )
    private LocalTime hora;

}
