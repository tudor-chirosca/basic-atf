package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto;
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface AlertsApi {

  @ApiOperation("Fetch Priorities AlertTypes and Thresholds")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Reference Alerts fetched successfully", response = AlertReferenceDataDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<AlertReferenceDataDto> getReferenceAlerts(ClientType clientType, String context);

  @ApiOperation("Fetch Alert Stats and total")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Alert stats fetched successfully", response = AlertStatsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<AlertStatsDto> getAlertStats(ClientType clientType, String context);

  @ApiOperation("Fetches alerts based on filters")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Alerts successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<AlertDto>> getAlerts(final ClientType clientType, final String context,
      @Valid final AlertSearchRequest searchRequest, final HttpServletRequest request);
}