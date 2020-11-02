package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AlertsApi {

  @ApiOperation("Fetch Priorities AlertTypes and Thresholds")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Reference Alerts fetched successfully", response = AlertReferenceDataDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<AlertReferenceDataDto> getReferenceAlerts(
      final HttpServletRequest httpServletRequest);

  @ApiOperation("Fetch Alert Stats and total")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Alert stats fetched successfully", response = AlertStatsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<AlertStatsDto> getAlertStats(
      final HttpServletRequest httpServletRequest);
}


