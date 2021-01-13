package com.vocalink.crossproduct.infrastructure.bps.position;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.INTRA_DAY_POSITION_GROSS_PATH;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.IntraDayPositionGrossRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    IntraDayPositionRequest body = IntraDayPositionRequest.builder()
        .schemeCode(BPSConstants.SCHEME_CODE)
        .participantId(participantId)
        .build();

    return webClient.post()
        .uri(resolve(INTRA_DAY_POSITION_GROSS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(body), IntraDayPositionRequest.class))
        .retrieve()
        .bodyToFlux(BPSIntraDayPositionGross.class)
        .retryWhen(BPSRetryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER::toEntity)
        .collectList()
        .block();
  }
}
