package com.vocalink.crossproduct.infrastructure.bps.io;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.IO_DETAILS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.IO_PARTICIPANTS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.io.IODashboard;
import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIODataRepository;
import com.vocalink.crossproduct.ui.dto.EmptyBody;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import com.vocalink.crossproduct.ui.dto.io.IORequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSParticipantIODataRepository implements ParticipantIODataRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public IODashboard findAll() {
    return webClient.post()
        .uri(resolve(IO_PARTICIPANTS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(EmptyBody.builder().build()), EmptyBody.class))
        .retrieve()
        .bodyToMono(BPSIODashboard.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public IODetails findByParticipantId(String participantId) {
    final IORequest request = new IORequest(participantId);
    return webClient.post()
        .uri(resolve(IO_DETAILS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), IORequest.class))
        .retrieve()
        .bodyToMono(BPSIODetails.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
