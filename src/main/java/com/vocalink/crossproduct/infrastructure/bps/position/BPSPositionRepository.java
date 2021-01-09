package com.vocalink.crossproduct.infrastructure.bps.position;

import static com.vocalink.crossproduct.infrastructure.bps.config.PathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.POSITION_DETAILS_PATH;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.adapter.bps.BPSProperties;
import com.vocalink.crossproduct.domain.position.ParticipantPosition;
import com.vocalink.crossproduct.domain.position.PositionDetails;
import com.vocalink.crossproduct.domain.position.PositionRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

  private List<BPSPositionDetails> getBpsPositionsDetails(String participantId) {
    PositionRequest body = PositionRequest.builder()
        .schemeCode(BPSConstants.SCHEME_CODE)
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
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
