package com.meepleconnect.boardgamesapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String devUrl = "http://localhost:8080";
    private static final String prodUrl = "http://production.host";

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Development server for MeepleConnect");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Production server for MeepleConnect");

        Contact contact = new Contact();
        contact.setEmail("student@novi-education.nl");
        contact.setName("Novi Student");
        contact.setUrl("https://www.novi.nl");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("MeepleConnect Boardgame API")
                .version("1.0")
                .contact(contact)
                .description("API for managing boardgames, orders, and customers in a boardgame shop.")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}
