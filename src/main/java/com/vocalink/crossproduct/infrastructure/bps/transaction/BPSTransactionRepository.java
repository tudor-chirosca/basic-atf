package com.vocalink.crossproduct.infrastructure.bps.transaction;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.SINGLE_TRANSACTION_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.TRANSACTION_ENQUIRIES_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Result;
import com.vocalink.crossproduct.domain.transaction.Transaction;
import com.vocalink.crossproduct.domain.transaction.TransactionEnquirySearchCriteria;
import com.vocalink.crossproduct.domain.transaction.TransactionRepository;
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
public class BPSTransactionRepository implements TransactionRepository {

  private static final String OFFSET = "offset";
  private static final String PAGE_SIZE = "pageSize";

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public Result<Transaction> findPaginated(TransactionEnquirySearchCriteria criteria) {
    final String currency = bpsProperties.getCurrencies().get(bpsProperties.getSchemeCode());
    final BPSTransactionEnquirySearchRequest bpsRequest = BPSMAPPER.toBps(criteria, currency);
    final URI uri = UriComponentsBuilder.fromUri(resolve(TRANSACTION_ENQUIRIES_PATH, bpsProperties))
        .queryParam(OFFSET, criteria.getOffset())
        .queryParam(PAGE_SIZE, criteria.getLimit())
        .build().toUri();
    return webClient.post()
        .uri(uri)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSTransactionEnquirySearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSResult<BPSTransaction>>() {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(t -> MAPPER.toEntity(t, Transaction.class))
        .block();
  }

  @Override
  public Transaction findById(String id) {
    final BPSSingleTransactionRequest bpsRequest = new BPSSingleTransactionRequest(id);
    return webClient.post()
        .uri(resolve(SINGLE_TRANSACTION_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(bpsRequest), BPSSingleTransactionRequest.class))
        .retrieve()
        .bodyToMono(BPSTransaction.class)
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
