package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.facade.InputOutputFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class InputOutputController implements InputOutputApi {

  private final InputOutputFacade inputOutputFacade;

  @GetMapping(value = "/io", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IODashboardDto> getSettlement(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      @RequestParam(required = false) String timestamp) {

    LocalDate dateFrom = getDate(timestamp);

    IODashboardDto settlementDto = inputOutputFacade
        .getInputOutputDashboard(context.toUpperCase(), clientType, dateFrom);

    return ResponseEntity.ok().body(settlementDto);
  }

  @GetMapping(value = "/io-details/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IODetailsDto> getIODetails(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      @PathVariable String participantId,
      @RequestParam(required = false) String timestamp) {

    LocalDate dateFrom = getDate(timestamp);

    IODetailsDto ioDetails = inputOutputFacade
        .getInputOutputDetails(context.toUpperCase(), clientType, dateFrom, participantId);

    return ResponseEntity.ok().body(ioDetails);
  }

  private LocalDate getDate(String timestamp) {
    return StringUtils.isEmpty(timestamp)
        ? LocalDate.now()
        : LocalDate.parse(timestamp);
  }
}
