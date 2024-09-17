package com.learning.account_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountServiceApplication {

    public static void main(String[] args) {
        initDbConfiguration();
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    /**
     * Init db configuration variables.
     */
    private static void initDbConfiguration() {
        Dotenv dotenv = Dotenv.load();
        final String DB_URL = dotenv.get("DB_URL");
        final String DB_USERNAME = dotenv.get("DB_USERNAME");
        final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

        System.setProperty("datasource_url", DB_URL);
        System.setProperty("datasource_username", DB_USERNAME);
        System.setProperty("datasource_password", DB_PASSWORD);
    }
}
