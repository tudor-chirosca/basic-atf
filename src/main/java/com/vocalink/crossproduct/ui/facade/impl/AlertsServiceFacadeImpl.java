package com.vocalink.crossproduct.ui.facade.impl;

import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.infrastructure.exception.EntityNotFoundException;
import com.vocalink.crossproduct.repository.AlertsRepository;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
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
}
