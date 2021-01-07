package com.vocalink.crossproduct.domain.alert;

import com.vocalink.crossproduct.domain.Page;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchParams;
import java.util.Optional;

public interface AlertsRepository {

  Optional<AlertReferenceData> findReferenceAlerts(String context);

  Optional<AlertStats> findAlertStats(String context);

  Page<Alert> findAlerts(String context, AlertSearchParams searchParams);
}