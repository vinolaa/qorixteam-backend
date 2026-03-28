package com.github.ovinola.qorixteam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todos os endpoints REST da aplicação
                // Permite conexões do Next.js (em dev) e do próprio WebSocket
                .allowedOriginPatterns("http://localhost:3000", "http://localhost:8080", "*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*")
                .allowCredentials(true); // Necessário se formos usar cookies ou auth via sessão futuramente
    }

}
