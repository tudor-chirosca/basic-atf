package com.vocalink.crossproduct.infrastructure.bps.io;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.IO_DETAILS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.IO_PARTICIPANTS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.io.IODetails;
import com.vocalink.crossproduct.domain.io.ParticipantIOData;
import com.vocalink.crossproduct.domain.io.ParticipantIODataRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import com.vocalink.crossproduct.ui.dto.io.IORequest;
import java.time.LocalDate;
import java.util.List;
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
  public List<ParticipantIOData> findByTimestamp(LocalDate localDate) {
    return webClient.get()
        .uri(resolve(IO_PARTICIPANTS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .bodyToFlux(BPSParticipantIOData.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .collectList()
        .block();
  }

  @Override
  public List<IODetails> findIODetailsFor(String participantId, LocalDate localDate) {
    final IORequest request = new IORequest(bpsProperties.getSchemeCode(), participantId,
        localDate.toString());
    return webClient.post()
        .uri(resolve(IO_DETAILS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), IORequest.class))
        .retrieve()
        .bodyToFlux(BPSIODetails.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .collectList()
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
