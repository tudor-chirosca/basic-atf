package com.vocalink.crossproduct.infrastructure.factory;

import static java.util.stream.Collectors.toMap;

import com.vocalink.crossproduct.infrastructure.exception.ClientNotAvailableException;
import com.vocalink.crossproduct.shared.alert.AlertsClient;
import com.vocalink.crossproduct.shared.batch.BatchesClient;
import com.vocalink.crossproduct.shared.files.FilesClient;
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
  private final List<AlertsClient> alertsClientList;
  private final List<FilesClient> filesClientList;
  private final List<BatchesClient> batchesClientList;
  private final List<SettlementsClient> settlementsClientList;

  private Map<String, ParticipantIODataClient> participantIODataClients;
  private Map<String, AlertsClient> alertsClients;
  private Map<String, FilesClient> filesClients;
  private Map<String, BatchesClient> batchesClients;
  private Map<String, SettlementsClient> settlementsClients;

  @PostConstruct
  public void init() {
    participantIODataClients = participantIODataClientsList.stream()
        .collect(toMap(ParticipantIODataClient::getContext, Function.identity()));
    alertsClients = alertsClientList.stream()
        .collect(toMap(AlertsClient::getContext, Function.identity()));
    filesClients = filesClientList.stream()
        .collect(toMap(FilesClient::getContext, Function.identity()));
    batchesClients = batchesClientList.stream()
        .collect(toMap(BatchesClient::getContext, Function.identity()));
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

  public BatchesClient getBatchesClient(String context) {
    if (batchesClients.get(context) == null) {
      throw new ClientNotAvailableException("Batches client not available for context " + context);
    }
    return batchesClients.get(context);
  }

  public SettlementsClient getSettlementsClient(String context) {
    if (settlementsClients.get(context) == null) {
      throw new ClientNotAvailableException("Settlements client not available for context " + context);
    }
    return settlementsClients.get(context);
  }
}
