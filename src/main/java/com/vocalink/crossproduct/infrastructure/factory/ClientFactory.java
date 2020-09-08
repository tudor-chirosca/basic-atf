package com.vocalink.crossproduct.infrastructure.factory;

import com.vocalink.crossproduct.infrastructure.exception.CyclesClientNotAvailableException;
import com.vocalink.crossproduct.infrastructure.exception.ParticipantClientNotAvailableException;
import com.vocalink.crossproduct.infrastructure.exception.ParticipantIODataClientNotAvailableException;
import com.vocalink.crossproduct.shared.cycle.CyclesClient;
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient;
import com.vocalink.crossproduct.shared.participant.ParticipantClient;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientFactory {

  private final List<ParticipantClient> participantClientList;
  private final List<CyclesClient> cyclesClientList;
  private final List<ParticipantIODataClient> participantIODataClientsList;

  private Map<String, ParticipantClient> participantsClients;
  private Map<String, CyclesClient> cyclesClients;
  private Map<String, ParticipantIODataClient> participantIODataClients;

  @PostConstruct
  public void init() {
    participantsClients = participantClientList.stream()
        .collect(Collectors.toMap(ParticipantClient::getContext, Function.identity()));
    cyclesClients = cyclesClientList.stream()
        .collect(Collectors.toMap(CyclesClient::getContext, Function.identity()));
    participantIODataClients = participantIODataClientsList.stream()
        .collect(Collectors.toMap(ParticipantIODataClient::getContext, Function.identity()));
  }

  public ParticipantClient getParticipantClient(String context) {
    if (participantsClients.get(context) == null) {
      throw new ParticipantClientNotAvailableException("Participant client not available for context " + context);
    }
    return participantsClients.get(context);
  }

  public CyclesClient getCyclesClient(String context) {
    if (cyclesClients.get(context) == null) {
      throw new CyclesClientNotAvailableException("Cycles client not available for context " + context);
    }
    return cyclesClients.get(context);
  }

  public ParticipantIODataClient getParticipantIODataClient(String context) {
    if (participantIODataClients.get(context) == null) {
      throw new ParticipantIODataClientNotAvailableException("Participant IO data client not available for context " + context);
    }
    return participantIODataClients.get(context);
  }
}
