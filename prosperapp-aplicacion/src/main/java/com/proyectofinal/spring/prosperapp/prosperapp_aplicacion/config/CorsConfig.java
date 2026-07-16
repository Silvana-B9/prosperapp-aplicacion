package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * El frontend (HTML/CSS/JS puro) se abre desde un archivo local o un
 * servidor de desarrollo distinto al del backend, por lo que el navegador
 * bloquea las llamadas fetch() a menos que el backend permita el origen.
 * Al ser un proyecto academico local se permite cualquier origen solo
 * para las rutas /api/**.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
