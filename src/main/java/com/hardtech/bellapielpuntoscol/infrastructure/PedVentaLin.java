package com.hardtech.bellapielpuntoscol.infrastructure;


import lombok.Getter;
import lombok.Setter;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity(name = "PEDVENTALIN")
@Getter
@Setter
@IdClass(PedVentaLinId.class)
public class PedVentaLin {


    @Id
    @Column(name = "NUMSERIE")
    private String numSerie;

    @Id
    @Column(name = "NUMPEDIDO")
    private int numPedido;

    @Id
    @Column(name = "CODARTICULO")
    private String codArticulo;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "UNIDADESTOTAL")
    private int unidadesTotal;

    @Column(name = "TOTAL")
    private double total;

    @Column(name = "CODVENDEDOR")
    private String codVendedor;

    @Column(name = "PRECIO")
    private double precio;

    @Override
    public String toString(){
        return
                codArticulo +
                ", Descripcion: " + descripcion +
                ", Unidades: " + unidadesTotal +
                ", Precio:" + precio +
                ", Vendedor:" + codVendedor;
    }



}
