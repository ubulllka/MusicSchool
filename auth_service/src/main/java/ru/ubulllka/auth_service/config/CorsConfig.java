package ru.ubulllka.auth_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Определяет путь, для которого разрешены запросы CORS
                .allowedOrigins("http://localhost:8080") // Определяет разрешенные источники (домены) для CORS-запросов
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Определяет разрешенные HTTP-методы
                .allowedHeaders("*"); // Определяет разрешенные заголовки
    }
}