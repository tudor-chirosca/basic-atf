package com.vocalink.crossproduct.infrastructure.adapter;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.infrastructure.factory.ClientFactory;
import com.vocalink.crossproduct.domain.alert.AlertsRepository;
import com.vocalink.crossproduct.shared.alert.AlertsClient;
import com.vocalink.crossproduct.shared.alert.CPAlertParams;
import com.vocalink.crossproduct.shared.alert.CPAlertRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchParams;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.presenter.mapper.DTOMapper;
import java.util.Optional;
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

    return client.findReferenceAlerts().map(EntityMapper.MAPPER::toEntity);
  }

  @Deprecated
  @Override
  public Page<Alert> findAlerts(String context, AlertSearchRequest searchRequest) {
    log.info("Fetching alerts from context {} ...", context);

    final CPAlertRequest request = DTOMapper.MAPPER.toDto(searchRequest);

    AlertsClient client = clientFactory.getAlertsClient(context);

    return EntityMapper.MAPPER.toEntityAlert(client.findAlerts(request));
  }

  @Override
  public Page<Alert> findAlerts(String context, AlertSearchParams searchParams) {
    log.info("Fetching alerts from context {} ...", context);

    final CPAlertParams cpAlertParams = DTOMapper.MAPPER.toDto(searchParams);

    AlertsClient client = clientFactory.getAlertsClient(context);

    return EntityMapper.MAPPER.toEntityAlert(client.findAlerts(cpAlertParams));
  }

  @Override
  public Optional<AlertStats> findAlertStats(String context) {
    log.info("Fetching alert stats from context {} ...", context);

    AlertsClient client = clientFactory.getAlertsClient(context);

    return client.findAlertStats().map(EntityMapper.MAPPER::toEntity);
  }
}
