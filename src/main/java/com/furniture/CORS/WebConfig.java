package com.furniture.CORS;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Allow all API endpoints under /api/*
            .allowedOrigins("http://localhost:5173") // Allow requests from your frontend
            .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow specific HTTP methods
            .allowCredentials(true) // Allow sending credentials (cookies, headers)
            .allowedHeaders("*"); // Allow all headers
    }
}
