package com.javaapp.api_banking;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;

import io.swagger.v3.oas.annotations.info.Info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Banking API",
                version = "1.0",
                description = "Back-office banking system (Accounts, Transactions, Logs)"
        )
)
public class ApiBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiBankingApplication.class, args);
    }

}
