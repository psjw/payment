package com.project.insure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CardPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardPaymentApplication.class, args);
    }

}
