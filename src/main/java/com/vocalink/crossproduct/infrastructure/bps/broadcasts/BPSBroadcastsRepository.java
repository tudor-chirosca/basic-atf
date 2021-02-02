package com.vocalink.crossproduct.infrastructure.bps.broadcasts;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.BROADCASTS_PATH;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.broadcasts.Broadcast;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsRepository;
import com.vocalink.crossproduct.domain.broadcasts.BroadcastsSearchCriteria;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSBroadcastsRepository implements BroadcastsRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public Page<Broadcast> findPaginated(BroadcastsSearchCriteria criteria) {
    final BPSBroadcastsSearchRequest request = BPSMAPPER.toBps(criteria);
    return webClient.post()
        .uri(resolve(BROADCASTS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSBroadcastsSearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSBroadcast>>() {
        })
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER::toPagedBroadcastEntity)
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
