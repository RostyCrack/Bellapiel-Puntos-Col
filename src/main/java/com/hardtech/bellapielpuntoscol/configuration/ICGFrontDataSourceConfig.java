package com.hardtech.bellapielpuntoscol.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix="icgfront.datasource")
@Getter
@Setter
public class ICGFrontDataSourceConfig {
    private String url;
	private String password;
	private String username;
}