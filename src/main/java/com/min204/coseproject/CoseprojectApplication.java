package com.min204.coseproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CoseprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoseprojectApplication.class, args);
    }

}
