package com.vocalink.crossproduct.infrastructure;

import static java.util.Arrays.asList;

import com.vocalink.crossproduct.domain.Participant;
import com.vocalink.crossproduct.domain.ParticipantRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class APIParticipantRepository implements ParticipantRepository {

  private final ApiClient apiClient;

  @Override
  public List<Participant> fetchParticipants() {
    log.info("Fetching participants..");
    return asList(apiClient.fetchParticipants().block());
  }
}
