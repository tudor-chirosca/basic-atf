package com.vocalink.portal.infrastructure;

import com.vocalink.portal.domain.Participant;
import com.vocalink.portal.domain.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
@Slf4j
public class APIParticipantRepository implements ParticipantRepository {

  private final ApiClient apiClient;

  @Override
  public Mono<Participant[]> fetchParticipants() {
    log.info("Fetching participants..");
    return apiClient.fetchParticipants();
  }
}
