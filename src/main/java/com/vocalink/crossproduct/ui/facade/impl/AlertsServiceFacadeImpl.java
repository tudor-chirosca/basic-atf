package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.repository.AlertsRepository;
import com.vocalink.crossproduct.ui.dto.alert.AlertDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.facade.AlertsServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import com.vocalink.crossproduct.ui.presenter.PresenterFactory;
import java.util.List;
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
  public AlertDataDto getAlerts(String context, ClientType clientType, AlertSearchRequest request) {

    List<Alert> alerts = alertsRepository.findAlerts(context, request);

    List<AlertDto> alertsDto = presenterFactory.getPresenter(clientType)
        .presentAlert(alerts);

    return new AlertDataDto(alertsDto.size(), alertsDto);
  }
}
