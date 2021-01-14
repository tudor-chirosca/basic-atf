package com.vocalink.crossproduct.infrastructure.factory;

import static java.util.stream.Collectors.toMap;

import com.vocalink.crossproduct.infrastructure.exception.ClientNotAvailableException;
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient;
import com.vocalink.crossproduct.shared.settlement.SettlementsClient;
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
  private final List<SettlementsClient> settlementsClientList;

  private Map<String, ParticipantIODataClient> participantIODataClients;
  private Map<String, SettlementsClient> settlementsClients;

  @PostConstruct
  public void init() {
    participantIODataClients = participantIODataClientsList.stream()
        .collect(toMap(ParticipantIODataClient::getContext, Function.identity()));
    settlementsClients = settlementsClientList.stream()
        .collect(toMap(SettlementsClient::getContext, Function.identity()));
  }

  public ParticipantIODataClient getParticipantIODataClient(String context) {
    if (participantIODataClients.get(context) == null) {
      throw new ClientNotAvailableException(
          "Participant IO data client not available for context " + context);
    }
    return participantIODataClients.get(context);
  }

  public SettlementsClient getSettlementsClient(String context) {
    if (settlementsClients.get(context) == null) {
      throw new ClientNotAvailableException("Settlements client not available for context " + context);
    }
    return settlementsClients.get(context);
  }
}
