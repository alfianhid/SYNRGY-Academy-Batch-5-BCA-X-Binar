package com.binarxbca.chapter6.config;

import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI demoApi(@Value("REST API for Chapter 6") String appDescription,
                           @Value("v1.0.0") String appVersion) {

        final String securitySchemeName = "bearerAuth";
        Server server = new Server();
        server.setUrl("https://alfianhid-challenge6-production-53ac.up.railway.app/");
        List<Server> listOfServer = new ArrayList<>();
        listOfServer.add(server);

        return new OpenAPI()
                .info(new Info()
                        .title("Chapter 6")
                        .version(appVersion)
                        .description(appDescription)
                        .termsOfService("http://swagger.io/terms")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .servers(listOfServer)
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName)
                )
                .components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .description("Please input generated token you get after login...")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
