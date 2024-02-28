package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity(name = "PEDVENTACAB")
@Getter
@Setter
@IdClass(PedVentaCabId.class)
public class PedVentaCab {

    @Column(name = "CODCLIENTE")
    private int codCliente;

    @Id
    @Column(name = "NUMSERIE")
    private String numSerie;

    @Id
    @Column(name = "NUMPEDIDO")
    private int numPedido;

    @Column(name = "TOTNETO")
    private double totNeto;



}
