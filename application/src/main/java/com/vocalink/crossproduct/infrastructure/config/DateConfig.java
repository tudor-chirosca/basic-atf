package com.vocalink.crossproduct.infrastructure.config;

import java.time.Clock;

import static java.time.Clock.systemUTC;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateConfig {

    @Bean
    public Clock clock() {
        return systemUTC();
    }
}
