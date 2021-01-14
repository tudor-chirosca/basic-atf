package com.vocalink.crossproduct.ui.facade.impl;

import static com.vocalink.crossproduct.infrastructure.adapter.EntityMapper.MAPPER;

import com.vocalink.crossproduct.RepositoryFactory;
import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertSearchCriteria;
import com.vocalink.crossproduct.domain.alert.AlertStats;
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

  private final PresenterFactory presenterFactory;
  private final RepositoryFactory repositoryFactory;

  @Override
  public AlertReferenceDataDto getAlertsReference(String product, ClientType clientType) {

    final AlertReferenceData alertReferenceData = repositoryFactory.getAlertsClient(product)
        .findAlertsReferenceData();

    return presenterFactory.getPresenter(clientType)
        .presentAlertReference(alertReferenceData);
  }

  @Override
  public AlertStatsDto getAlertStats(String product, ClientType clientType) {

    final AlertStats alertStats = repositoryFactory.getAlertsClient(product).findAlertStats();

    return presenterFactory.getPresenter(clientType)
        .presentAlertStats(alertStats);
  }

  @Override
  public PageDto<AlertDto> getAlerts(String product, ClientType clientType,
      AlertSearchRequest requestDto) {

    final AlertSearchCriteria request = MAPPER.toEntity(requestDto);
    final Page<Alert> alerts = repositoryFactory.getAlertsClient(product).findPaginated(request);

    return presenterFactory.getPresenter(clientType).presentAlert(alerts);
  }
}
