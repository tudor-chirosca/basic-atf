package com.vocalink.portal.ui.controllers;

import com.vocalink.portal.application.PositionsService;
import com.vocalink.portal.ui.dto.SettlementDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class PositionsController {

  private final PositionsService positionsService;

  @GetMapping(value = "/settlement", produces = MediaType.APPLICATION_JSON_VALUE)
  public SettlementDto getSettlement() {
    log.info("Fetching positions controller..");
    return positionsService.getSettlement();
  }

  @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
  public String healthCheck() {
    return "OK";
  }
}
