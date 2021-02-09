package com.vocalink.crossproduct.infrastructure.bps.participant;

import static com.vocalink.crossproduct.infrastructure.bps.config.BPSPathUtils.resolve;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.MANAGED_PARTICIPANT_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.PARTICIPANTS_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.PARTICIPANT_CONFIGURATION_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.config.ResourcePath.PARTICIPANT_PATH;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.BPSMapper.BPSMAPPER;
import static com.vocalink.crossproduct.infrastructure.bps.mappers.EntityMapper.MAPPER;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.participant.ManagedParticipantsSearchCriteria;
import com.vocalink.crossproduct.domain.participant.Participant;
import com.vocalink.crossproduct.domain.participant.ParticipantConfiguration;
import com.vocalink.crossproduct.domain.participant.ParticipantRepository;
import com.vocalink.crossproduct.infrastructure.bps.BPSPage;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
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
        .map(MAPPER::toEntity)
        .block();
  }

  @Override
  public Page<Participant> findPaginated(ManagedParticipantsSearchCriteria criteria) {
    BPSManagedParticipantsSearchRequest request = BPSMAPPER.toBps(criteria);
    return webClient.post()
        .uri(resolve(MANAGED_PARTICIPANT_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSManagedParticipantsSearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSParticipant>>() {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntityParticipant)
        .block();
  }

  @Override
  public ParticipantConfiguration findConfigurationById(String participantId) {
    BPSParticipantConfigurationRequest request = new BPSParticipantConfigurationRequest(participantId);
    return webClient.post()
        .uri(resolve(PARTICIPANT_CONFIGURATION_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSParticipantConfigurationRequest.class))
        .retrieve()
        .bodyToMono(BPSParticipantConfiguration.class)
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(MAPPER::toEntity)
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
        .map(MAPPER::toEntity)
        .collectList()
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
