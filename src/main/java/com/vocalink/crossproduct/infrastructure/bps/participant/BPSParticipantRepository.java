package com.vocalink.crossproduct.infrastructure.bps.participant;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.PARTICIPANTS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.PARTICIPANT_PATH;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
@Slf4j
public class BPSParticipantRepository implements ParticipantRepository {

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public List<Participant> findByConnectingPartyAndType(String connectingParty, String participantType) {
    final BPSParticipantsSearchRequest bpsRequest = BPSMAPPER.toBps(connectingParty, participantType);
    return findParticipantsWith(bpsRequest);
  }

  @Override
  public List<Participant> findAll() {
    return findParticipantsWith(new BPSParticipantsSearchRequest());
  }

  @Override
  public Participant findById(final String participantId) {
    final BPSParticipantSearchRequest body = new BPSParticipantSearchRequest(participantId);
    return webClient.post()
        .uri(resolve(PARTICIPANT_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(body), BPSParticipantSearchRequest.class))
        .retrieve()
        .bodyToMono(BPSParticipant.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER::toEntity)
        .block();
  }

  private List<Participant> findParticipantsWith(final BPSParticipantsSearchRequest request) {
    return webClient.post()
        .uri(resolve(PARTICIPANTS_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSParticipantsSearchRequest.class))
        .retrieve()
        .bodyToFlux(BPSParticipant.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(BPSMAPPER::toEntity)
        .collectList()
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
