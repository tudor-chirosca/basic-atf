package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchParams;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface AlertsServiceFacade {

  AlertReferenceDataDto getAlertsReference(String context, ClientType clientType);

  AlertStatsDto getAlertStats(String context, ClientType clientType);

  @Deprecated
  PageDto<AlertDto> getAlerts(String context, ClientType clientType, AlertSearchRequest request);

  PageDto<AlertDto> getAlerts(String context, ClientType clientType, AlertSearchParams searchParams);
}
