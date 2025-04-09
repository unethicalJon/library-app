package com.example.library.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management System API")
                        .version("1.0")
                        .description("API documentation for managing libraries, books, orders and users" +
                                "; authentication and authorization, generating statistics," +
                                " exporting excel files and also printing invoices in pdf.")
                        .contact(new Contact()
                                .name("Fatjon Bunguri")
                                .email("jonibunguri@yahoo.com")
                                .url("https://www.linkedin.com/in/fatjon-bunguri-0b2b4b280/")));
    }
}
