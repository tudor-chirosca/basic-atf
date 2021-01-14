package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface AlertsServiceFacade {

  AlertReferenceDataDto getAlertsReference(String product, ClientType clientType);

  AlertStatsDto getAlertStats(String product, ClientType clientType);

  PageDto<AlertDto> getAlerts(String product, ClientType clientType, AlertSearchRequest searchParams);
}
