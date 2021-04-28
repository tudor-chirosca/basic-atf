package com.vocalink.crossproduct.domain.alert;

import com.vocalink.crossproduct.domain.CrossproductRepository;
import com.vocalink.crossproduct.domain.Page;

public interface AlertRepository extends CrossproductRepository {

  AlertReferenceData findAlertsReferenceData();

  AlertStats findAlertStats();

  Page<Alert> findPaginated(AlertSearchCriteria criteria);
}