package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

interface InputOutputApi {

  @ApiOperation("Fetch input output data")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Input output retrieved successfully", response = IODashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<IODashboardDto> getSettlement(HttpServletRequest httpServletRequest,
      String timestamp);


  @ApiOperation("Fetch input output data")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Input output retrieved successfully", response = IODashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<IODetailsDto> getIODetails(HttpServletRequest httpServletRequest,
      String participantId,
      String timestamp);
}
