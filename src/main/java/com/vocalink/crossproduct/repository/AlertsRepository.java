package com.vocalink.crossproduct.repository;

import com.vocalink.crossproduct.domain.alert.AlertReferenceData;
import java.util.Optional;

public interface AlertsRepository {

  Optional<AlertReferenceData> findReferenceAlerts(String context);
}
