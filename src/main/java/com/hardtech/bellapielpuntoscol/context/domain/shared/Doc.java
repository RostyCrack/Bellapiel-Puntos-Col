package com.hardtech.bellapielpuntoscol.context.domain.shared;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "doc")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class Doc {

    @XmlElement(name = "bd")
    private BD bd;

    @XmlElement(name = "codvendedor")
    private String codvendedor;

    @XmlElement(name = "tipodoc")
    private String tipodoc;

    @XmlElement(name = "idtipodoc")
    private int idtipodoc;

    @XmlElement(name = "serie")
    private String serie;

    @XmlElement(name = "numero")
    private int numero;

    @XmlElement(name = "n")
    private String n;

    @XmlElement(name = "GuardandoTef")
    private int guardandoTef;

    @XmlElement(name = "GuardandoTef2")
    private int guardandoTef2;

    @XmlElement(name = "guardandodocumentoabonar")
    private int guardandoDocumentoAbonar;


}
