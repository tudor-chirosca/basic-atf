package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementCycleDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementScheduleDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
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
      final ParticipantSettlementRequest settlementRequest,
      final HttpServletRequest request);

  @ApiOperation("Searchable list fo settlement cycles in which a given participant has instructions")
  @ApiResponses({
          @ApiResponse(code = 200, message = "Settlements fetched successfully", response = PageDto.class),
          @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<ParticipantSettlementCycleDto>> getSettlements(
          ClientType clientType, String context, @Valid final SettlementEnquiryRequest enquiryRequest, HttpServletRequest request);

  @ApiOperation("Fetch latest settlement cycles")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Settlements fetched successfully", response = LatestSettlementCyclesDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<LatestSettlementCyclesDto> getLatestSettlementCycles(
      ClientType clientType, String context
  );

  @ApiOperation("Scheme level settlement schedule")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Settlement schedules fetched successfully", response = SettlementScheduleDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<SettlementScheduleDto> getSettlementsSchedule(
      ClientType clientType, String context);
}
