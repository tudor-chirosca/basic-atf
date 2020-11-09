package com.vocalink.crossproduct.infrastructure.adapter;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.repository.AlertsRepository;
import com.vocalink.crossproduct.shared.alert.AlertRequest;
import com.vocalink.crossproduct.shared.alert.AlertsClient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class AlertsAdapter implements AlertsRepository {

  private final ClientFactory clientFactory;

  @Override
  public Optional<AlertReferenceData> findReferenceAlerts(String context) {
    log.info("Fetching reference alerts from context {} ...", context);

    AlertsClient client = clientFactory.getAlertsClient(context);

    return client.findReferenceAlerts().map(MAPPER::toEntity);
  }

  @Override
  public List<Alert> findAlerts(String context, AlertRequest request) {
    log.info("Fetching alerts from context {} ...", context);

    AlertsClient client = clientFactory.getAlertsClient(context);

    return client.findAlerts(request).stream()
        .map(MAPPER::toEntity)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<AlertStats> findAlertStats(String context) {
    log.info("Fetching alert stats from context {} ...", context);

    AlertsClient client = clientFactory.getAlertsClient(context);

    return client.findAlertStats().map(MAPPER::toEntity);
  }
}
