package com.khotixs.media_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Allow all paths
                .allowedOrigins("http://localhost:3000") // Allow only the ngrok URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "*")  // Allow headers needed for requests
                .allowCredentials(true);  // Allow credentials if needed
    }
}
