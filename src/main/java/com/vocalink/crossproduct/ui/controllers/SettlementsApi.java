package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface SettlementsApi {

  @ApiOperation("Fetch Settlement Cycle Details")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Settlement cycle details fetched successfully",
          response = ParticipantSettlementDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<ParticipantSettlementDetailsDto> getSettlementCycleDetails(
      final ClientType clientType,
      final String context,
      final String cycleId,
      final String participantId,
      final ParticipantSettlementRequest request);
}
