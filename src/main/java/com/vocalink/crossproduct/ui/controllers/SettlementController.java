package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.application.SettlementService;
import com.vocalink.crossproduct.ui.dto.SettlementDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SettlementController {

  private final SettlementService settlementService;

  @ApiOperation("Fetch settlement information")
  @GetMapping(value = "/settlement", produces = MediaType.APPLICATION_JSON_VALUE)
  public SettlementDto getSettlement() {
    log.info("Fetching positions controller..");
    return settlementService.getSettlement();
  }

  @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
  public String healthCheck() {
    return "OK";
  }
}
