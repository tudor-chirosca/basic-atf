package com.vocalink.crossproduct.ui.controllers;

import static java.util.Objects.isNull;

import com.vocalink.crossproduct.infrastructure.exception.InvalidRequestParameterException;
import com.vocalink.crossproduct.ui.controllers.api.SettlementsApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementScheduleDto;
import com.vocalink.crossproduct.ui.facade.api.SettlementsFacade;
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

    ParticipantSettlementDetailsDto settlementDetails = settlementsFacade.getSettlementDetails(context,
        clientType, request, cycleId, participantId);

    return ResponseEntity.ok().body(settlementDetails);
  }

  @GetMapping(value = "/enquiry/settlements", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<ParticipantSettlementCycleDto>> getSettlements(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final SettlementEnquiryRequest request) {

    if (isNull(request.getParticipants()) || request.getParticipants().isEmpty()) {
      throw new InvalidRequestParameterException("participants is missing in request params");
    }

    PageDto<ParticipantSettlementCycleDto> settlements =
        settlementsFacade.getSettlements(context, clientType, request);

    return ResponseEntity.ok().body(settlements);
  }

  @GetMapping(value = "/enquiry/settlements/cycles", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<LatestSettlementCyclesDto> getLatestSettlementCycles(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context) {

    LatestSettlementCyclesDto latestCycles = settlementsFacade.getLatestCycles(context, clientType);
    return ResponseEntity.ok().body(latestCycles);
  }

  @GetMapping(value = "/enquiry/settlements/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SettlementScheduleDto> getSettlementsSchedule(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context) {

    final SettlementScheduleDto settlementsSchedule = settlementsFacade
        .getSettlementsSchedule(context, clientType);

    return ResponseEntity.ok().body(settlementsSchedule);
  }
}
