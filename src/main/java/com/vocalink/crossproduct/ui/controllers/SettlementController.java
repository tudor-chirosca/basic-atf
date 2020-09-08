package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto;
import com.vocalink.crossproduct.ui.facade.SettlementServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SettlementController {

  private final SettlementServiceFacade settlementServiceFacade;

  @ApiOperation("Fetch settlement, including positions, participants and cycles")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Settlement retrieved successfully", response = SettlementDashboardDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  @GetMapping(value = "/settlement", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SettlementDashboardDto> getSettlement(HttpServletRequest httpServletRequest) {
    String contextHeader = httpServletRequest.getHeader("context");
    String clientTypeHeader = httpServletRequest.getHeader("client-type");

    ClientType clientType = (StringUtils.isEmpty(clientTypeHeader))
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    SettlementDashboardDto settlementDashboardDto = settlementServiceFacade
        .getSettlement(contextHeader.toUpperCase(), clientType);

    return ResponseEntity.ok().body(settlementDashboardDto);
  }
}
