package com.hardtech.bellapielpuntoscol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class BellapielpuntoscolApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BellapielpuntoscolApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }

}
