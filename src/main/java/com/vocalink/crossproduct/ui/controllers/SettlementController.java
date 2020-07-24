package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.SettlementDto;
import com.vocalink.crossproduct.ui.facade.SettlementServiceFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
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

  @ApiOperation("Fetch settlement information")
  @GetMapping(value = "/settlement", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SettlementDto> getSettlement(HttpServletRequest httpServletRequest) {
    String context = httpServletRequest.getHeader("context");
    String clientTypeHeader = httpServletRequest.getHeader("client-type");

    if (StringUtils.isEmpty(context)) {
      return ResponseEntity.badRequest().build();
    }

    ClientType clientType = (clientTypeHeader == null)
        ? ClientType.UI
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    SettlementDto settlementDto = settlementServiceFacade.getSettlement(context.toUpperCase(), clientType);

    return ResponseEntity.ok().body(settlementDto);
  }

  @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
  public String healthCheck() {
    return "OK";
  }
}
