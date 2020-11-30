package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.domain.alert.AlertsRepository;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.facade.AlertsServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertsServiceFacadeImpl implements AlertsServiceFacade {

  private final AlertsRepository alertsRepository;
  private final PresenterFactory presenterFactory;

  @Override
  public AlertReferenceDataDto getAlertsReference(String context, ClientType clientType) {

    AlertReferenceData alertReferenceData = alertsRepository.findReferenceAlerts(context)
        .orElseThrow(() -> new EntityNotFoundException("No alert references were found"));

    return presenterFactory.getPresenter(clientType)
        .presentAlertReference(alertReferenceData);
  }

  @Override
  public AlertStatsDto getAlertStats(String context, ClientType clientType) {

    AlertStats alertStats = alertsRepository.findAlertStats(context)
        .orElseThrow(() -> new EntityNotFoundException("No alert stats were found"));

    return presenterFactory.getPresenter(clientType)
        .presentAlertStats(alertStats);
  }

  @Override
  public PageDto<AlertDto> getAlerts(String context, ClientType clientType, AlertSearchRequest request) {

    Page<Alert> alerts = alertsRepository.findAlerts(context, request);

    return presenterFactory.getPresenter(clientType).presentAlert(alerts);
  }
}
