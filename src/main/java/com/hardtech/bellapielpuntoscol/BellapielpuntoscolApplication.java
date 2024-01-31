package com.hardtech.bellapielpuntoscol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class BellapielpuntoscolApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BellapielpuntoscolApplication.class);
        springApplication.run(args);
        LocalDateTime date = LocalDateTime.of(2021, 8, 1, 0, 0, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = date.format(formatter);
        System.out.println("Application started at " + formattedDate +" "+ date.toString());
    }

}
