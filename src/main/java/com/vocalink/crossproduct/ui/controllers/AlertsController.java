package com.vocalink.crossproduct.ui.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.AlertsApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.facade.api.AlertsServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;

import static com.vocalink.crossproduct.ui.aspects.EventType.VIEW_ALERTS;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AlertsController implements AlertsApi {

  private final AlertsServiceFacade alertsServiceFacade;

  @GetMapping(value = "/reference/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AlertReferenceDataDto> getReferenceAlerts(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context) {

    AlertReferenceDataDto alertReferenceDataDto = alertsServiceFacade
        .getAlertsReference(context, clientType);

    return ResponseEntity.ok().body(alertReferenceDataDto);
  }

  @GetMapping(value = "/alerts/stats", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AlertStatsDto> getAlertStats(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context) {

    AlertStatsDto alertStatsDto = alertsServiceFacade.getAlertStats(context, clientType);

    return ResponseEntity.ok().body(alertStatsDto);
  }

  @Auditable(type = VIEW_ALERTS, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<AlertDto>> getAlerts(
          @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
          AlertSearchRequest searchRequest, final HttpServletRequest request) {

    PageDto<AlertDto> alertDataDto = alertsServiceFacade
            .getAlerts(context, clientType, searchRequest);

    return ResponseEntity.ok().body(alertDataDto);
  }
}