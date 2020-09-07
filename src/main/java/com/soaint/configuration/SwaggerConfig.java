package com.soaint.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

   // Para acceder a swagger: http://localhost:8080/swagger-ui.html#/

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.soaint.controller"))
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag("CHATBOT", "SERVIDOR", 1))
                .tags(new Tag("USERS", "USUARIOS", 2))
                .tags(new Tag("LISTS", "SERVICIOS", 3))
                .tags(new Tag("MENU", "MENU DEL SISTEMA", 4))
                .tags(new Tag("CLIENTS", "REGISTRO DE CLIENTES", 5))
                .tags(new Tag("LOOK AND FEEL", "REGISTRO DE AVATARES, COLORES", 6))
                .tags(new Tag("QUIZ", "REGISTRO DE QUIZ", 7));
    }

}
