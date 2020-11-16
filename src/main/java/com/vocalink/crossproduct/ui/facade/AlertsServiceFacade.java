package com.vocalink.crossproduct.ui.facade;

import com.vocalink.crossproduct.ui.dto.alert.AlertDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;

public interface AlertsServiceFacade {

  AlertReferenceDataDto getAlertsReference(String context, ClientType clientType);

  AlertStatsDto getAlertStats(String context, ClientType clientType);

  AlertDataDto getAlerts(String context, ClientType clientType, AlertSearchRequest request);
}
