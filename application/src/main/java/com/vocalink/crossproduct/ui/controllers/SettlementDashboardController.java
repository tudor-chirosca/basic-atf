package com.vocalink.crossproduct.ui.controllers;

import static com.vocalink.crossproduct.ui.aspects.EventType.VIEW_SETTLEMENT_DASHBOARD_BY_MESSAGE_TYPE;
import static com.vocalink.crossproduct.ui.aspects.EventType.VIEW_SETTLEMENT_DASHBOARD;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.SettlementDashboardApi;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementDashboardRequest;
import com.vocalink.crossproduct.ui.facade.api.SettlementDashboardFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SettlementDashboardController implements SettlementDashboardApi {

  private final SettlementDashboardFacade settlementDashboardFacade;

  @Auditable(type = VIEW_SETTLEMENT_DASHBOARD, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/settlement", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SettlementDashboardDto> getSettlement(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final SettlementDashboardRequest settlementDashboardRequest,
      final HttpServletRequest httpServletRequest) {
    log.debug("Request: {}", settlementDashboardRequest);

    final SettlementDashboardDto settlementDashboardDto = settlementDashboardFacade
        .getParticipantSettlement(context.toUpperCase(), clientType, settlementDashboardRequest);

    return ResponseEntity.ok().body(settlementDashboardDto);
  }

  @Auditable(type = VIEW_SETTLEMENT_DASHBOARD_BY_MESSAGE_TYPE, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/settlement/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ParticipantDashboardSettlementDetailsDto> getSettlementDetails(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String participantId,
      final HttpServletRequest httpServletRequest) {

    final ParticipantDashboardSettlementDetailsDto selfFundingDetailsDto = settlementDashboardFacade
        .getParticipantSettlementDetails(context.toUpperCase(), clientType, participantId);

    return ResponseEntity.ok().body(selfFundingDetailsDto);
  }
}
