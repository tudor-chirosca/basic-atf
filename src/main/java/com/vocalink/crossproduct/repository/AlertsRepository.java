package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.alert.Alert;
import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import com.vocalink.crossproduct.domain.alert.AlertStats;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import java.util.List;
import java.util.Optional;

public interface AlertsRepository {

  Optional<AlertReferenceData> findReferenceAlerts(String context);

  Optional<AlertStats> findAlertStats(String context);

  List<Alert> findAlerts(String context, AlertSearchRequest request);
}
