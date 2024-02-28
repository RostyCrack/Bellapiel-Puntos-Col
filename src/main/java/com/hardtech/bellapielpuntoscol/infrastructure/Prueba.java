package com.hardtech.bellapielpuntoscol.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Entity
public class Prueba {


    @Id
    @Column(name = "hola")
    private int hola;



}
