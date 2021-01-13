package com.vocalink.crossproduct.infrastructure.bps.batch;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.BATCH_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SINGLE_BATCH_PATH;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.batch.Batch;
import com.vocalink.crossproduct.domain.batch.BatchEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.batch.BatchRepository;
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
public class BPSBatchRepository implements BatchRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public Page<Batch> findPaginated(BatchEnquirySearchCriteria request) {
    final BPSBatchEnquirySearchRequest bpsRequest = BPSMAPPER.toBps(request);
    return webClient.post()
        .uri(resolve(BATCH_ENQUIRIES_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSBatchEnquirySearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSBatch>>() {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER::toBatchPageEntity)
        .block();
  }

  @Override
  public Batch findById(String id) {
    final BPSSingleBatchRequest bpsRequest = new BPSSingleBatchRequest(id);
    return  webClient.post()
        .uri(resolve(SINGLE_BATCH_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSSingleBatchRequest.class))
        .retrieve()
        .bodyToMono(BPSBatch.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .map(BPSMAPPER::toEntity)
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
