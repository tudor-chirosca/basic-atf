package com.vocalink.crossproduct.infrastructure.bps.position;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.PathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.INTRA_DAY_POSITION_GROSS_PATH;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.position.IntraDayPositionGross;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.position.PositionRepository;
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
public class BPSPositionRepository implements PositionRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig BPSRetryWebClientConfig;
  private final WebClient webClient;

  @Override
  public List<PositionDetails> findByParticipantId(String schemeParticipantIdentifier) {
    return getBpsPositionsDetails(schemeParticipantIdentifier).stream()
        .map(this::mapPosition)
        .collect(toList());
  }

  @Override
  public List<IntraDayPositionGross> findIntraDayPositionsGrossByParticipantId(
      List<String> participantId) {
    IntraDayPositionRequest body = IntraDayPositionRequest.builder()
        .schemeCode(BPSConstants.SCHEME_CODE)
        .schemeParticipantIdentifiers(participantId)
        .build();

    return webClient.post()
        .uri(resolve(INTRA_DAY_POSITION_GROSS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(body), IntraDayPositionRequest.class))
        .retrieve()
        .bodyToFlux(BPSIntraDayPositionGross.class)
        .retryWhen(BPSRetryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER)
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
        .retryWhen(BPSRetryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .collectList()
        .block();
  }

  private PositionDetails mapPosition(BPSPositionDetails bpsPositionDetails) {
    return PositionDetails.builder()
        .sessionCode(bpsPositionDetails.getSessionCode())
        .customerCreditTransfer(
            ParticipantPosition.builder()
                .participantId(bpsPositionDetails.getSchemeParticipantIdentifier())
                .credit(bpsPositionDetails.getCustomerCreditTransfer().getCredit())
                .debit(bpsPositionDetails.getCustomerCreditTransfer().getDebit())
                .netPosition(bpsPositionDetails.getCustomerCreditTransfer().getNetPosition())
                .build())
        .paymentReturn(ParticipantPosition.builder()
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
