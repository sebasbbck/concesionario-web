package com.sebas.concesionario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Punto de entrada. Extiende SpringBootServletInitializer para que el WAR también
 * arranque desplegado en un Tomcat externo (sin el método main).
 */
@SpringBootApplication
public class ConcesionarioApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ConcesionarioApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ConcesionarioApplication.class);
    }
}
