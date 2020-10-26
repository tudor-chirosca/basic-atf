package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.facade.SettlementServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SettlementController implements SettlementApi {

  private final SettlementServiceFacade settlementServiceFacade;

  @GetMapping(value = "/settlement/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SettlementDashboardDto> getSettlement(
      HttpServletRequest httpServletRequest,
      final @PathVariable String participantId) {
    String contextHeader = httpServletRequest.getHeader("context");
    String clientTypeHeader = httpServletRequest.getHeader("client-type");

    ClientType clientType = (StringUtils.isEmpty(clientTypeHeader))
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    SettlementDashboardDto settlementDashboardDto = settlementServiceFacade
        .getSettlement(contextHeader.toUpperCase(), clientType, participantId);
    return ResponseEntity.ok().body(settlementDashboardDto);
  }

  @GetMapping(value = "/settlement", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SettlementDashboardDto> getSettlement(
      HttpServletRequest httpServletRequest) {
    String contextHeader = httpServletRequest.getHeader("context");
    String clientTypeHeader = httpServletRequest.getHeader("client-type");

    ClientType clientType = (StringUtils.isEmpty(clientTypeHeader))
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    SettlementDashboardDto settlementDashboardDto = settlementServiceFacade
        .getSettlement(contextHeader.toUpperCase(), clientType);
    return ResponseEntity.ok().body(settlementDashboardDto);
  }

  @GetMapping(value = "/settlementDetails/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Deprecated
  public ResponseEntity<ParticipantSettlementDetailsDto> getSelfFundingSettlementDetails(
      HttpServletRequest httpServletRequest, final @PathVariable String participantId) {

    String contextHeader = httpServletRequest.getHeader("context");
    String clientTypeHeader = httpServletRequest.getHeader("client-type");

    ClientType clientType = (StringUtils.isEmpty(clientTypeHeader))
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    ParticipantSettlementDetailsDto selfFundingDetailsDto = settlementServiceFacade
        .getParticipantSettlementDetails(contextHeader.toUpperCase(), clientType, participantId);

    return ResponseEntity.ok().body(selfFundingDetailsDto);
  }

  @GetMapping(value = "/settlement-details/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ParticipantSettlementDetailsDto> getSettlementDetails(
      HttpServletRequest httpServletRequest, final @PathVariable String participantId) {

    String contextHeader = httpServletRequest.getHeader("context");
    String clientTypeHeader = httpServletRequest.getHeader("client-type");

    ClientType clientType = (StringUtils.isEmpty(clientTypeHeader))
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    ParticipantSettlementDetailsDto selfFundingDetailsDto = settlementServiceFacade
        .getParticipantSettlementDetails(contextHeader.toUpperCase(), clientType, participantId);

    return ResponseEntity.ok().body(selfFundingDetailsDto);
  }
}
