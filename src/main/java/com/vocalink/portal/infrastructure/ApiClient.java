package com.vocalink.portal.infrastructure;

import com.vocalink.portal.domain.Cycle;
import com.vocalink.portal.domain.Participant;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ApiClient {

  @Value("${api.url}")
  private String url;

  public WebClient webClient;

  @PostConstruct
  public void initialize() {
    webClient = WebClient.builder().baseUrl(url).build();
  }

  public Mono<Participant[]> fetchParticipants() {
    return webClient
        .get()
        .uri("/participants")
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(Participant[].class);
  }

  public Mono<Cycle[]> fetchCycles() {
    return webClient
        .get()
        .uri("/cycles")
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToMono(Cycle[].class)
        .map(mono -> {
          log.info("Obtained cycles");
          return mono;
        });
  }

}
