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
import com.vocalink.crossproduct.infrastructure.bps.BPSResult;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSConstants;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties;
import com.vocalink.crossproduct.infrastructure.bps.config.BPSRetryWebClientConfig;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.infrastructure.exception.ExceptionUtils;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
@Slf4j
public class BPSParticipantRepository implements ParticipantRepository {

  private static final String OFFSET = "offset";
  private static final String PAGE_SIZE = "pageSize";

  private final BPSProperties bpsProperties;
  private final BPSRetryWebClientConfig retryWebClientConfig;
  private final WebClient webClient;

  @Override
  public Page<Participant> findByConnectingPartyAndType(String connectingParty, String participantType) {
    final BPSParticipantsSearchRequest bpsRequest = BPSMAPPER.toBps(connectingParty, participantType);
    return findParticipantsWith(bpsRequest);
  }

  @Override
  @Cacheable(value = "participantCache", key = "#root.method.name")
  public Page<Participant> findAll() {
    return findParticipantsWith(new BPSParticipantsSearchRequest());
  }

  @Override
  @Cacheable(value = "participantCache", key = "#participantId")
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
  public Optional<Participant> findByBic(final String participantBic) {
    return this.findAll().getItems().stream()
        .filter(p -> p.getBic().equalsIgnoreCase(participantBic))
        .findFirst();
  }

  @Override
  public Page<Participant> findPaginated(ManagedParticipantsSearchCriteria criteria) {
    BPSManagedParticipantsSearchRequest request = BPSMAPPER.toBps(criteria);
    return webClient.post()
        .uri(resolve(MANAGED_PARTICIPANT_PATH, bpsProperties))
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSManagedParticipantsSearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSPage<BPSManagedParticipant>>() {})
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(p -> MAPPER.toEntity(p, Participant.class))
        .block();
  }

  @Override
  public ParticipantConfiguration findConfigurationById(String participantId) {
    BPSParticipantConfigurationRequest request = BPSParticipantConfigurationRequest.builder()
        .schemeParticipantIdentifier(participantId)
        .build();
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

  private Page<Participant> findParticipantsWith(final BPSParticipantsSearchRequest request) {
    final URI uri = UriComponentsBuilder.fromUri(resolve(PARTICIPANTS_PATH, bpsProperties))
        .queryParam(OFFSET, 0)
        .queryParam(PAGE_SIZE, Integer.MAX_VALUE)
        .build().toUri();
    return webClient.post()
        .uri(uri)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .body(fromPublisher(Mono.just(request), BPSParticipantsSearchRequest.class))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<BPSResult<BPSParticipant>>() {
        })
        .retryWhen(retryWebClientConfig.fixedRetry())
        .doOnError(ExceptionUtils::raiseException)
        .map(f -> MAPPER.toEntity(f, Participant.class))
        .block();
  }

  @Override
  public String getProduct() {
    return BPSConstants.PRODUCT;
  }
}
