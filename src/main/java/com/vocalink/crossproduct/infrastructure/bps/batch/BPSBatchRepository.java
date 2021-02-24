package com.vocalink.crossproduct.infrastructure.bps.batch;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.BATCH_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SINGLE_BATCH_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Result;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.batch.BatchRepository;
import com.vocalink.crossproduct.infrastructure.bps.BPSResult;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSBatchRepository implements BatchRepository {

  private static final String OFFSET = "offset";
  private static final String PAGE_SIZE = "pageSize";

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public Result<Batch> findPaginated(BatchEnquirySearchCriteria request) {
    final BPSBatchEnquirySearchRequest bpsRequest = BPSMAPPER.toBps(request);
    final URI uri = UriComponentsBuilder.fromUri(resolve(BATCH_ENQUIRIES_PATH, bpsProperties))
        .queryParam(OFFSET, request.getOffset())
        .queryParam(PAGE_SIZE, request.getLimit())
        .build().toUri();
    return webClient.post()
        .uri(uri)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSBatchEnquirySearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSResult<BPSBatchPart>>() {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(b -> MAPPER.toEntity(b, Batch.class))
        .block();
  }

  @Override
  public Batch findById(String id) {
    final BPSSingleBatchRequest bpsRequest = new BPSSingleBatchRequest(null, id);
    return  webClient.post()
        .uri(resolve(SINGLE_BATCH_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSSingleBatchRequest.class))
        .retrieve()
        .bodyToMono(BPSBatchDetailed.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
