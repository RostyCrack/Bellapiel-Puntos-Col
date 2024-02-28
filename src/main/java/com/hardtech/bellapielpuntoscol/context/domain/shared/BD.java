package com.hardtech.bellapielpuntoscol.context.domain.shared;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
public class BD {
    @XmlElement(name = "server")
    private String server;
    @XmlElement(name = "database")
    private String database;
    @XmlElement(name = "user")
    private String user;

    // Add getters and setters for the fields
    // ...
}
