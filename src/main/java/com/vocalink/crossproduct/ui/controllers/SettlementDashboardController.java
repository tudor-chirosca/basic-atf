package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.controllers.api.SettlementDashboardApi;
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.facade.api.SettlementDashboardFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SettlementDashboardController implements SettlementDashboardApi {

  private final SettlementDashboardFacade settlementDashboardFacade;

  @GetMapping(value = "/settlement", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SettlementDashboardDto> getSettlement(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @RequestParam(required = false) String fundingParticipantId) {

    final SettlementDashboardDto settlementDashboardDto = settlementDashboardFacade
        .getParticipantSettlement(context.toUpperCase(), clientType, fundingParticipantId);

    return ResponseEntity.ok().body(settlementDashboardDto);
  }

  @GetMapping(value = "/settlement/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ParticipantDashboardSettlementDetailsDto> getSettlementDetails(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String participantId) {

    final ParticipantDashboardSettlementDetailsDto selfFundingDetailsDto = settlementDashboardFacade
        .getParticipantSettlementDetails(context.toUpperCase(), clientType, participantId);

    return ResponseEntity.ok().body(selfFundingDetailsDto);
  }
}
