package com.vocalink.crossproduct.infrastructure.bps.routing;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.ROUTING_RECORD_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static java.util.stream.Collectors.toList;
import static java.util.Collections.singletonList;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import com.vocalink.crossproduct.domain.routing.RoutingRecordCriteria;
import com.vocalink.crossproduct.domain.routing.RoutingRepository;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSRoutingRepository implements RoutingRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  public static final String REACHABLE_BIC_SORT = "reachableBic";

  @Override
  public Page<RoutingRecord> findPaginated(RoutingRecordCriteria criteria) {
    BPSRoutingRecordRequest request = BPSMAPPER.toBps(criteria);
    return findRoutingRecordsBy(request);
  }

  @Override
  public List<RoutingRecord> findAllByBic(String bic) {
    BPSRoutingRecordRequest request = new BPSRoutingRecordRequest(
        0, Integer.MAX_VALUE, singletonList(REACHABLE_BIC_SORT), bic
    );
    return findRoutingRecordsBy(request).getItems();
  }

  public Page<RoutingRecord> findRoutingRecordsBy(BPSRoutingRecordRequest request) {
    return webClient.post()
        .uri(resolve(ROUTING_RECORD_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSRoutingRecordRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSRoutingRecord>>() {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(r ->
            new Page<>(r.getTotalResults(), r.getItems().stream()
                .map(MAPPER::toEntity)
                .collect(toList())))
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
