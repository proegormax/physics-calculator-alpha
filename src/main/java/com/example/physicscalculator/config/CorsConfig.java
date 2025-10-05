package com.example.physicscalculator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry r) {
        r.addMapping("/**")
                .allowedOrigins(
                       "https://proegormax.github.io"      // если это проектная страница
                )
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*");
        // .allowCredentials(true)  // включай ТОЛЬКО если используешь cookie-сессии
    }
}

