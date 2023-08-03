package com.hardtech.bellapielpuntoscol.context.domain.shared;

import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "docprinted")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class DocPrinted {
    @XmlElement(name = "bd")
    private BD bd;
    @XmlElement(name = "codvendedor")
    private String codvendedor;
    @XmlElement(name = "tipodoc")
    private String tipodoc;
    @XmlElement(name = "serie")
    private String serie;
    @XmlElement(name = "numero")
    private int numero;
    @XmlElement(name = "n")
    private String n;

    // Add getters and setters for the fields
    // ...
}

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
class BD {
    @XmlElement(name = "server")
    private String server;
    @XmlElement(name = "database")
    private String database;
    @XmlElement(name = "user")
    private String user;

    // Add getters and setters for the fields
    // ...
}
