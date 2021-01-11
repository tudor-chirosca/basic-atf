package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.facade.SettlementDashboardFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SettlementDashboardController implements SettlementDashboardApi {

  private final SettlementDashboardFacade settlementDashboardFacade;

  @GetMapping(value = "/settlement/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SettlementDashboardDto> getSettlement(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      @PathVariable String participantId) {

    SettlementDashboardDto settlementDashboardDto = settlementDashboardFacade
        .getParticipantSettlement(context.toUpperCase(), clientType, participantId);

    return ResponseEntity.ok().body(settlementDashboardDto);
  }

  @GetMapping(value = "/settlement", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SettlementDashboardDto> getSettlement(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context) {

    SettlementDashboardDto settlementDashboardDto = settlementDashboardFacade
        .getSettlement(context.toUpperCase(), clientType);

    return ResponseEntity.ok().body(settlementDashboardDto);
  }

  @GetMapping(value = "/settlementDetails/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Deprecated
  public ResponseEntity<ParticipantDashboardSettlementDetailsDto> getSelfFundingSettlementDetails(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      @PathVariable String participantId) {

    ParticipantDashboardSettlementDetailsDto selfFundingDetailsDto = settlementDashboardFacade
        .getParticipantSettlementDetails(context.toUpperCase(), clientType, participantId);

    return ResponseEntity.ok().body(selfFundingDetailsDto);
  }

  @GetMapping(value = "/settlement-details/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ParticipantDashboardSettlementDetailsDto> getSettlementDetails(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      @PathVariable String participantId) {

    ParticipantDashboardSettlementDetailsDto selfFundingDetailsDto = settlementDashboardFacade
        .getParticipantSettlementDetails(context.toUpperCase(), clientType, participantId);

    return ResponseEntity.ok().body(selfFundingDetailsDto);
  }
}
