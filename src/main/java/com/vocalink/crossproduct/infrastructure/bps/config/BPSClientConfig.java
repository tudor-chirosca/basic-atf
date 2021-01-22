package com.vocalink.crossproduct.infrastructure.bps.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.channel.BootstrapHandlers;
import reactor.netty.http.client.HttpClient;

@Configuration
public class BPSClientConfig {

  @Bean
  public WebClient webClient(ObjectMapper mapper, HttpClient httpClient) {
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
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClient.create()
        .tcpConfiguration(
            tc -> tc.bootstrap(
                b -> BootstrapHandlers.updateLogSupport(b, new BPSHttpLogger(HttpClient.class))));
  }
}
