package com.vocalink.crossproduct.infrastructure.factory;

import com.vocalink.crossproduct.shared.cycle.CyclesClient;
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

  private Map<String, ParticipantClient> participantsClients;
  private Map<String, CyclesClient> cyclesClients;

  @PostConstruct
  public void init() {
    participantsClients = participantClientList.stream()
        .collect(Collectors.toMap(ParticipantClient::getContext, Function.identity()));
    cyclesClients = cyclesClientList.stream()
        .collect(Collectors.toMap(CyclesClient::getContext, Function.identity()));
  }

  public ParticipantClient getParticipantClient(String context) {
    if (participantsClients.get(context) == null) {
      throw new RuntimeException("Participant client not available for context " + context);
    }
    return participantsClients.get(context);
  }

  public CyclesClient getCyclesClient(String context) {
    if (cyclesClients.get(context) == null) {
      throw new RuntimeException("Cycles client not available for context " + context);
    }
    return cyclesClients.get(context);
  }
}
