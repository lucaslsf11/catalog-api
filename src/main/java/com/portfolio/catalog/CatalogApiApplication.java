package com.portfolio.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CatalogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogApiApplication.class, args);
    }
}