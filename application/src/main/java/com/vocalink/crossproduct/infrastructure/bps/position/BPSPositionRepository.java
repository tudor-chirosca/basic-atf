package com.vocalink.crossproduct.infrastructure.bps.position;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SETTLEMENT_POSITION_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.position.PositionRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSPositionRepository implements PositionRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public List<ParticipantPosition> findByParticipantId(String schemeParticipantIdentifier) {
    final BPSPositionsRequest request = BPSPositionsRequest.builder()
        .schemeCode(bpsProperties.getSchemeCode())
        .participantIds(singletonList(schemeParticipantIdentifier))
        .build();

    return Objects.requireNonNull(webClient.post()
        .uri(resolve(SETTLEMENT_POSITION_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSPositionsRequest.class))
        .retrieve()
        .onStatus(s -> s.equals(HttpStatus.NOT_FOUND) || s.equals(HttpStatus.NO_CONTENT), r ->
            Mono.error(new EntityNotFoundException()))
        .bodyToMono(BPSSettlementPositionWrapper.class)
        .onErrorResume(EntityNotFoundException.class, e ->
            Mono.just(BPSSettlementPositionWrapper.builder()
                .schemeId(bpsProperties.getSchemeCode())
                .mlSettlementPositions(emptyList())
                .build()))
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .block());
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
