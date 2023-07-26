package com.hardtech.bellapielpuntoscol.infrastructure;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

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
