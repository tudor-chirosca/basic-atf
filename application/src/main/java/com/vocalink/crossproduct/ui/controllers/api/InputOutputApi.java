package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface InputOutputApi {

  @ApiOperation("Fetch input output data")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Input output retrieved successfully", response = IODashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<IODashboardDto> getSettlement(final ClientType clientType, final String context,
      final String timestamp, final HttpServletRequest httpServletRequest);


  @ApiOperation("Fetch input output data")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Input output retrieved successfully", response = IODashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<IODetailsDto> getIODetails(final ClientType clientType, final String context,
      final String participantId, final String forDate,
      final HttpServletRequest httpServletRequest);
}
