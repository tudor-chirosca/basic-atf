package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.facade.AlertsServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AlertsController implements AlertsApi {

  private final AlertsServiceFacade facade;

  @GetMapping(value = "/reference/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AlertReferenceDataDto> getReferenceAlerts(
      final HttpServletRequest httpServletRequest) {
    String contextHeader = httpServletRequest.getHeader("context");
    String clientTypeHeader = httpServletRequest.getHeader("client-type");

    ClientType clientType = (StringUtils.isEmpty(clientTypeHeader))
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    AlertReferenceDataDto alertReferenceDataDto = facade
        .getAlertsReference(contextHeader, clientType);

    return ResponseEntity.ok().body(alertReferenceDataDto);
  }

  @GetMapping(value = "/alerts/stats", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AlertStatsDto> getAlertStats(HttpServletRequest httpServletRequest) {
    String contextHeader = httpServletRequest.getHeader("context");
    String clientTypeHeader = httpServletRequest.getHeader("client-type");

    ClientType clientType = (StringUtils.isEmpty(clientTypeHeader))
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    AlertStatsDto alertStatsDto = facade.getAlertStats(contextHeader, clientType);

    return ResponseEntity.ok().body(alertStatsDto);
  }
}
