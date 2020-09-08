package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.facade.InputOutputFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class InputOutputController {

  private final InputOutputFacade inputOutputFacade;

  @ApiOperation("Fetch input output data")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Input output retrieved successfully", response = IODashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  @GetMapping(value = "/io", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IODashboardDto> getSettlement(HttpServletRequest httpServletRequest,
      @RequestParam(required = false) String timestamp) {
    String contextHeader = httpServletRequest.getHeader("context");
    String clientTypeHeader = httpServletRequest.getHeader("client-type");

    LocalDate dateFrom = StringUtils.isEmpty(timestamp)
        ? LocalDate.now()
        : LocalDate.parse(timestamp);

    ClientType clientType = StringUtils.isEmpty(clientTypeHeader)
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    IODashboardDto settlementDto = inputOutputFacade
        .getInputOutputDashboard(contextHeader.toUpperCase(), clientType, dateFrom);

    return ResponseEntity.ok().body(settlementDto);
  }
}
