package com.vocalink.crossproduct.infrastructure.bps.repository;

import static com.vocalink.crossproduct.adapter.bps.CPMapper.MAPPER;
import static com.vocalink.crossproduct.adapter.bps.PathUtils.resolve;
import static com.vocalink.crossproduct.adapter.bps.ResourcePath.INTRA_DAY_POSITION_GROSS_PATH;
import static com.vocalink.crossproduct.adapter.bps.ResourcePath.POSITION_DETAILS_PATH;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.adapter.bps.BPSProperties;
import com.vocalink.crossproduct.adapter.bps.Constants;
import com.vocalink.crossproduct.adapter.bps.RetryWebClientConfig;
import com.vocalink.crossproduct.adapter.bps.exception.ExceptionUtils;
import com.vocalink.crossproduct.adapter.bps.positions.BPSIntraDayPositionGross;
import com.vocalink.crossproduct.adapter.bps.positions.BPSPositionDetails;
import com.vocalink.crossproduct.adapter.bps.requests.IntraDayPositionRequest;
import com.vocalink.crossproduct.adapter.bps.requests.PositionRequest;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.position.PositionRepository;
import com.vocalink.crossproduct.shared.positions.CPIntraDayPositionGross;
import com.vocalink.crossproduct.shared.positions.CPParticipantPosition;
import com.vocalink.crossproduct.shared.positions.CPPositionDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BPSPositionRepository implements PositionRepository {

  private final BPSProperties bpsProperties;
  private final RetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public List<PositionDetails> findByParticipantId(String schemeParticipantIdentifier) {
    return getBpsPositionsDetails(schemeParticipantIdentifier).stream()
        .map(this::mapPosition)
        .collect(toList());
  }

  @Override
  public List<CPIntraDayPositionGross> findIntraDayPositionsGrossByParticipantId(
      List<String> participantId) {
    IntraDayPositionRequest body = IntraDayPositionRequest.builder()
        .schemeCode(Constants.SCHEME_CODE)
        .schemeParticipantIdentifiers(participantId)
        .build();

    return webClient.post()
        .uri(resolve(INTRA_DAY_POSITION_GROSS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(body), IntraDayPositionRequest.class))
        .retrieve()
        .bodyToFlux(BPSIntraDayPositionGross.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toCp)
        .collectList()
        .block();
  }

  private List<BPSPositionDetails> getBpsPositionsDetails(String participantId) {
    PositionRequest body = PositionRequest.builder()
        .schemeCode(Constants.SCHEME_CODE)
        .schemeParticipantIdentifier(participantId)
        .build();
    return webClient.post()
        .uri(resolve(POSITION_DETAILS_PATH, bpsProperties))
        .accept(MediaType.APPLICATION_JSON)
        .body(fromPublisher(Mono.just(body), PositionRequest.class))
        .retrieve()
        .bodyToFlux(BPSPositionDetails.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .collectList()
        .block();
  }

  private CPPositionDetails mapPosition(BPSPositionDetails bpsPositionDetails) {
    return CPPositionDetails.builder()
        .sessionCode(bpsPositionDetails.getSessionCode())
        .customerCreditTransfer(CPParticipantPosition.builder()
            .participantId(bpsPositionDetails.getSchemeParticipantIdentifier())
            .credit(bpsPositionDetails.getCustomerCreditTransfer().getCredit())
            .debit(bpsPositionDetails.getCustomerCreditTransfer().getDebit())
            .netPosition(bpsPositionDetails.getCustomerCreditTransfer().getNetPosition())
            .build())
        .paymentReturn(CPParticipantPosition.builder()
            .participantId(bpsPositionDetails.getSchemeParticipantIdentifier())
            .credit(bpsPositionDetails.getPaymentReturn().getCredit())
            .debit(bpsPositionDetails.getPaymentReturn().getDebit())
            .netPosition(bpsPositionDetails.getPaymentReturn().getNetPosition())
            .build())
        .build();
  }

  @Override
  public String getContext() {
    return Constants.CONTEXT;
  }

  @Override
  public String getProduct() {
    return BPSConfig.PRODUCT_NAME;
  }
}
