//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(
        name = "ALBVENTACAB"
)
@IdClass(AlbVentaLinId.class)
@Getter
@Setter
@NoArgsConstructor
public class AlbVentaCab {
    @Id
    @Column(
            name = "NUMSERIE"
    )
    private String numSerie;
    @Id
    @Column(
            name = "NUMALBARAN"
    )
    private int numAlbaran;
    @Column(
            name = "NUMFAC"
    )
    private int numFactura;

}
