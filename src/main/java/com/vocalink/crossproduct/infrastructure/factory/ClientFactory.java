package com.vocalink.crossproduct.infrastructure.factory;

import com.vocalink.crossproduct.infrastructure.exception.ClientNotAvailableException;
import com.vocalink.crossproduct.shared.alert.AlertsClient;
import com.vocalink.crossproduct.shared.batch.BatchesClient;
import com.vocalink.crossproduct.shared.cycle.CyclesClient;
import com.vocalink.crossproduct.shared.files.FilesClient;
import com.vocalink.crossproduct.shared.io.ParticipantIODataClient;
import com.vocalink.crossproduct.shared.participant.ParticipantClient;
import com.vocalink.crossproduct.shared.positions.PositionClient;
import com.vocalink.crossproduct.shared.reference.ReferencesClient;
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
  private final List<PositionClient> positionClientList;
  private final List<AlertsClient> alertsClientList;
  private final List<FilesClient> filesClientList;
  private final List<ReferencesClient> referenceClientList;
  private final List<BatchesClient> batchesClientList;

  private Map<String, ParticipantClient> participantsClients;
  private Map<String, CyclesClient> cyclesClients;
  private Map<String, ParticipantIODataClient> participantIODataClients;
  private Map<String, PositionClient> positionClients;
  private Map<String, AlertsClient> alertsClients;
  private Map<String, FilesClient> filesClients;
  private Map<String, ReferencesClient> referenceClients;
  private Map<String, BatchesClient> batchesClients;

  @PostConstruct
  public void init() {
    participantsClients = participantClientList.stream()
        .collect(Collectors.toMap(ParticipantClient::getContext, Function.identity()));
    cyclesClients = cyclesClientList.stream()
        .collect(Collectors.toMap(CyclesClient::getContext, Function.identity()));
    participantIODataClients = participantIODataClientsList.stream()
        .collect(Collectors.toMap(ParticipantIODataClient::getContext, Function.identity()));
    positionClients = positionClientList.stream()
        .collect(Collectors.toMap(PositionClient::getContext, Function.identity()));
    alertsClients = alertsClientList.stream()
        .collect(Collectors.toMap(AlertsClient::getContext, Function.identity()));
    filesClients = filesClientList.stream()
        .collect(Collectors.toMap(FilesClient::getContext, Function.identity()));
    referenceClients = referenceClientList.stream()
        .collect(Collectors.toMap(ReferencesClient::getContext, Function.identity()));
    batchesClients = batchesClientList.stream()
        .collect(Collectors.toMap(BatchesClient::getContext, Function.identity()));
  }

  public ParticipantClient getParticipantClient(String context) {
    if (participantsClients.get(context) == null) {
      throw new ClientNotAvailableException(
          "Participant client not available for context " + context);
    }
    return participantsClients.get(context);
  }

  public CyclesClient getCyclesClient(String context) {
    if (cyclesClients.get(context) == null) {
      throw new ClientNotAvailableException("Cycles client not available for context " + context);
    }
    return cyclesClients.get(context);
  }

  public ParticipantIODataClient getParticipantIODataClient(String context) {
    if (participantIODataClients.get(context) == null) {
      throw new ClientNotAvailableException(
          "Participant IO data client not available for context " + context);
    }
    return participantIODataClients.get(context);
  }

  public PositionClient getPositionClient(String context) {
    if (positionClients.get(context) == null) {
      throw new ClientNotAvailableException("Position client not available for context " + context);
    }
    return positionClients.get(context);
  }

  public AlertsClient getAlertsClient(String context) {
    if (alertsClients.get(context) == null) {
      throw new ClientNotAvailableException("Alerts client not available for context " + context);
    }
    return alertsClients.get(context);
  }

  public FilesClient getFilesClient(String context) {
    if (filesClients.get(context) == null) {
      throw new ClientNotAvailableException("Files client not available for context " + context);
    }
    return filesClients.get(context);
  }

  public ReferencesClient getReferencesClient(String context) {
    if (referenceClients.get(context) == null) {
      throw new ClientNotAvailableException("References client not available for context " + context);
    }
    return referenceClients.get(context);
  }

  public BatchesClient getBatchesClient(String context) {
    if (batchesClients.get(context) == null) {
      throw new ClientNotAvailableException("Batches client not available for context " + context);
    }
    return batchesClients.get(context);
  }
}
