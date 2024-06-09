package com.sparta.icy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IcyApplication {

    public static void main(String[] args) {
        SpringApplication.run(IcyApplication.class, args);
    }

}