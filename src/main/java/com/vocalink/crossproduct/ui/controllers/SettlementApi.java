package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

interface SettlementApi {

  @ApiOperation("Fetch settlement, including positions, participants and cycles")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Settlement retrieved successfully", response = SettlementDashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<SettlementDashboardDto> getSettlement(ClientType clientType, String context,
      String participantId);

  @ApiOperation("Fetch settlement, including positions, participants and cycles")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Settlement retrieved successfully", response = SettlementDashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<SettlementDashboardDto> getSettlement(ClientType clientType, String context);

  @ApiOperation("Fetch self funded Settlement, including positions, participant info and cycles")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Settlement retrieved successfully", response = ParticipantSettlementDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<ParticipantSettlementDetailsDto> getSelfFundingSettlementDetails(
      ClientType clientType, String context, String participantId);

  @ApiOperation("Fetch self funded Settlement, including positions, participant info and cycles")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Settlement retrieved successfully", response = ParticipantSettlementDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<ParticipantSettlementDetailsDto> getSettlementDetails(
      ClientType clientType, String context, String participantId);
}
