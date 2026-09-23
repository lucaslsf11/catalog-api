package com.portfolio.catalog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catalog API - RESTful Service")
                        .version("v1.0.0")
                        .description("API REST para gerenciamento de catálogo de produtos e categorias construída com Java 21 e Spring Boot 3.")
                        .contact(new Contact()
                                .name("Lucas")
                                .url("https://github.com/"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}