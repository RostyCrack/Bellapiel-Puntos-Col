package com.hardtech.bellapielpuntoscol.context.domain.shared;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "docprinted")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
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
}
