//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity(
        name = "FACTURASVENTACAMPOSLIBRES"
)
@IdClass(FacturasVentaId.class)
public class FacturasVentaCamposLibres {
    @Id
    @Column(
            name = "NUMFACTURA"
    )
    private int numFactura;
    @Id
    @Column(
            name = "NUMSERIE"
    )
    private String numSerie;
    @Column(
            name = "FCHA_ACUMULACION"
    )
    private LocalDateTime fechaAcumulacionPuntos;
    @Column(
            name = "PUNTOS_ACUMULADOS"
    )
    private Integer puntosAcumulados;
    @OneToOne
    @JoinColumns({@JoinColumn(
            name = "NUMSERIE",
            referencedColumnName = "NUMSERIE"
    ), @JoinColumn(
            name = "NUMFACTURA",
            referencedColumnName = "NUMFACTURA"
    )})
    private FacturasVenta facturasVenta;
    @Column(
            name = "MENSAJE_PUNTOS"
    )
    private String mensajePuntos;

//    @Column(name = "PUNTOS_LOCATION_CODE")
//    private String locationCode;

    public FacturasVentaCamposLibres() {
    }

    public int getNumFactura() {
        return this.numFactura;
    }

    public String getNumSerie() {
        return this.numSerie;
    }

    public LocalDateTime getFechaAcumulacionPuntos() {
        return this.fechaAcumulacionPuntos;
    }

    public Integer getPuntosAcumulados() {
        return this.puntosAcumulados;
    }

    public FacturasVenta getFacturasVenta() {
        return this.facturasVenta;
    }

    public String getMensajePuntos() {
        return this.mensajePuntos;
    }

    public void setNumFactura(final int numFactura) {
        this.numFactura = numFactura;
    }

    public void setNumSerie(final String numSerie) {
        this.numSerie = numSerie;
    }

    public void setFechaAcumulacionPuntos(final LocalDateTime fechaAcumulacionPuntos) {
        this.fechaAcumulacionPuntos = fechaAcumulacionPuntos;
    }

    public void setPuntosAcumulados(final Integer puntosAcumulados) {
        this.puntosAcumulados = puntosAcumulados;
    }

    public void setFacturasVenta(final FacturasVenta facturasVenta) {
        this.facturasVenta = facturasVenta;
    }

    public void setMensajePuntos(final String mensajePuntos) {
        this.mensajePuntos = mensajePuntos;
    }


}
