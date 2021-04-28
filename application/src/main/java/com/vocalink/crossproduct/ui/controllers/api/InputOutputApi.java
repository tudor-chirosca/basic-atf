package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface InputOutputApi {

  @ApiOperation("Fetch input output data")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Input output retrieved successfully", response = IODashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<IODashboardDto> getSettlement(ClientType clientType, String context,
      String timestamp);


  @ApiOperation("Fetch input output data")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Input output retrieved successfully", response = IODashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<IODetailsDto> getIODetails(ClientType clientType, String context,
      String participantId, String forDate);
}
