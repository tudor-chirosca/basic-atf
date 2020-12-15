package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.facade.SettlementsFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SettlementsController implements SettlementsApi {

  private final SettlementsFacade settlementsFacade;

  @GetMapping(value = "/enquiry/settlements/{cycleId}/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ParticipantSettlementDetailsDto> getSettlementCycleDetails(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String cycleId,
      final @PathVariable String participantId,
      final ParticipantSettlementRequest request) {

    ParticipantSettlementDetailsDto participantSettlementDetailsDto = settlementsFacade.getDetailsBy(context,
        clientType, request, cycleId, participantId);

    return ResponseEntity.ok().body(participantSettlementDetailsDto);
  }
}
