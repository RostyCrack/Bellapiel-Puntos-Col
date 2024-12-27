package com.hardtech.bellapielpuntoscol.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "payment-methods")
public class PaymentMehtodProperties {
    private List<String> invalid;
}