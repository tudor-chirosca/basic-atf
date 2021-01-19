package com.vocalink.crossproduct.infrastructure.bps.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BPSClientConfig {

  @Bean
  public WebClient webClient(ObjectMapper mapper) {
    ExchangeStrategies strategies = ExchangeStrategies.builder()
        .codecs(clientCodecConfigurer -> {
              clientCodecConfigurer.defaultCodecs()
                  .jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
              clientCodecConfigurer.defaultCodecs()
                  .jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
            }
        ).build();
    return WebClient.builder()
        .exchangeStrategies(strategies)
        .build();
  }
}
