//package com.khotixs.portal_gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import java.util.List;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("http://localhost:3000")); // Allow specific origins
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE")); // Allow methods
//        config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Allow specific headers
//        config.setAllowCredentials(true);
//        source.registerCorsConfiguration("/**", config); // Apply globally
//        return source;
//    }
//}
