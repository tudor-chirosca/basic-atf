package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.alert.AlertDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.facade.AlertsServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AlertsController implements AlertsApi {

  private final AlertsServiceFacade alertsServiceFacade;

  @GetMapping(value = "/reference/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AlertReferenceDataDto> getReferenceAlerts(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context) {

    AlertReferenceDataDto alertReferenceDataDto = alertsServiceFacade.getAlertsReference(context, clientType);

    return ResponseEntity.ok().body(alertReferenceDataDto);
  }

  @GetMapping(value = "/alerts/stats", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AlertStatsDto> getAlertStats(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context) {

    AlertStatsDto alertStatsDto = alertsServiceFacade.getAlertStats(context, clientType);

    return ResponseEntity.ok().body(alertStatsDto);
  }

  @PostMapping(value = "/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AlertDataDto> getAlerts(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      final @RequestBody AlertSearchRequest request) {

    AlertDataDto alertDataDto = alertsServiceFacade.getAlerts(context, clientType, request);

    return ResponseEntity.ok().body(alertDataDto);
  }
}
