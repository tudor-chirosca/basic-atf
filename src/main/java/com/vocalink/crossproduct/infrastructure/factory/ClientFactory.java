package com.vocalink.crossproduct.infrastructure.factory;

import static java.util.stream.Collectors.toMap;

import com.vocalink.crossproduct.infrastructure.exception.ClientNotAvailableException;
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientFactory {

  private final List<ParticipantIODataClient> participantIODataClientsList;

  private Map<String, ParticipantIODataClient> participantIODataClients;

  @PostConstruct
  public void init() {
    participantIODataClients = participantIODataClientsList.stream()
        .collect(toMap(ParticipantIODataClient::getContext, Function.identity()));
  }

  public ParticipantIODataClient getParticipantIODataClient(String context) {
    if (participantIODataClients.get(context) == null) {
      throw new ClientNotAvailableException(
          "Participant IO data client not available for context " + context);
    }
    return participantIODataClients.get(context);
  }
}
