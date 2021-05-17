package com.vocalink.crossproduct.infrastructure.bps.config;

import static java.util.Arrays.asList;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.MediaType.parseMediaType;

import reactor.netty.channel.BootstrapHandlers;
import reactor.netty.http.client.HttpClient;

@Configuration
public class BPSClientConfig {

  private static final String TEXT_PLAIN_CHARSET_UTF_8 = "text/plain;charset=utf-8";

  @Bean
  public WebClient webClient(ObjectMapper mapper, HttpClient httpClient,
      ExchangeFilterFunction correlationFilter) {
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
        .filter(correlationFilter)
        .build();
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClient.create()
        .tcpConfiguration(
            tc -> tc.bootstrap(
                b -> BootstrapHandlers.updateLogSupport(b, new BPSHttpLogger(HttpClient.class))));
  }

  @Bean
  public RestTemplate restClient() {
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(buildJacksonMessageConverter());
      return restTemplate;
  }

  private HttpMessageConverter<?> buildJacksonMessageConverter() {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(asList(parseMediaType(TEXT_PLAIN_CHARSET_UTF_8), APPLICATION_OCTET_STREAM));
    return converter;
  }
}
