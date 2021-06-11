package com.vocalink.crossproduct.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty(value = "app.cors", havingValue = "true")
public class CorsConfig {

  @Value("${app.cors.hosts}")
  private String[] hosts;
  @Value("${app.cors.headers}")
  private String[] headers;

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedHeaders(headers)
            .allowedOrigins(hosts);
      }
    };
  }
}
