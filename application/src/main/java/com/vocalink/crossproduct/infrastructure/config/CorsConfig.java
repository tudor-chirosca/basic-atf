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

  @Bean
  public WebMvcConfigurer corsConfigurer(@Value("${app.corsHosts}") String[] corsHosts) {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedHeaders(
              "client-type",
              "context",
              "access-control-allow-headers",
              "content-type",
              "X-COMPANY-ID",
              "X-PARTICIPANT-ID",
              "X-ROLES",
              "X-POLLING-UI",
              "X-USER-ID"
            )
            .allowedOrigins(corsHosts);
      }
    };
  }
}
