package com.sebas.concesionario.config;

import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Path directorioFotos;

    public WebConfig(@Value("${concesionario.fotos.directorio}") String directorioFotos) {
        this.directorioFotos = Path.of(directorioFotos).toAbsolutePath().normalize();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /fotos/coches/golf.jpg -> datos/fotos/coches/golf.jpg (fuera del WAR, para poder subir fotos nuevas)
        registry.addResourceHandler("/fotos/**")
                .addResourceLocations(directorioFotos.toUri().toString());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/catalogo");
    }
}
