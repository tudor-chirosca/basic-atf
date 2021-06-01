package com.vocalink.crossproduct.infrastructure.bps.position;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.INTRA_DAY_POSITION_GROSS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGrossRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSIntradayPositionGrossRepository implements IntraDayPositionGrossRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig BPSRetryWebClientConfig;
  private final WebClient webClient;

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }

  @Override
  public List<IntraDayPositionGross> findById(String participantId) {
    BPSIntraDayPositionRequest body = new BPSIntraDayPositionRequest(
        bpsProperties.getSchemeCode(), participantId
    );
    return webClient.post()
        .uri(resolve(INTRA_DAY_POSITION_GROSS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(body), BPSIntraDayPositionRequest.class))
        .retrieve()
        .onStatus(s -> s.equals(HttpStatus.NOT_FOUND) || s.equals(HttpStatus.NO_CONTENT), r ->
            Mono.error(new EntityNotFoundException()))
        .bodyToFlux(BPSIntraDayPositionGross.class)
        .onErrorResume(EntityNotFoundException.class, e -> Mono.empty())
        .retryWhen(BPSRetryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
        .collectList()
        .block();
  }
}
