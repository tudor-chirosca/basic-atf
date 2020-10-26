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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class InputOutputController implements InputOutputApi {

  private final InputOutputFacade inputOutputFacade;

  @GetMapping(value = "/io", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IODashboardDto> getSettlement(HttpServletRequest httpServletRequest,
      @RequestParam(required = false) String timestamp) {

    String contextHeader = getHeader(httpServletRequest, "context");
    String clientTypeHeader = getHeader(httpServletRequest, "client-type");
    LocalDate dateFrom = getDate(timestamp);

    ClientType clientType = StringUtils.isEmpty(clientTypeHeader)
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    IODashboardDto settlementDto = inputOutputFacade
        .getInputOutputDashboard(contextHeader.toUpperCase(), clientType, dateFrom);

    return ResponseEntity.ok().body(settlementDto);
  }

  @GetMapping(value = "/io-details/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IODetailsDto> getIODetails(HttpServletRequest httpServletRequest,
      final @PathVariable String participantId,
      @RequestParam(required = false) String timestamp) {

    String contextHeader = getHeader(httpServletRequest, "context");
    String clientTypeHeader = getHeader(httpServletRequest, "client-type");
    LocalDate dateFrom = getDate(timestamp);

    ClientType clientType = StringUtils.isEmpty(clientTypeHeader)
        ? ClientType.SYSTEM
        : ClientType.valueOf(clientTypeHeader.toUpperCase());

    IODetailsDto ioDetails = inputOutputFacade
        .getInputOutputDetails(contextHeader.toUpperCase(), clientType, dateFrom, participantId);

    return ResponseEntity.ok().body(ioDetails);
  }

  private LocalDate getDate(String timestamp) {
    return StringUtils.isEmpty(timestamp)
        ? LocalDate.now()
        : LocalDate.parse(timestamp);
  }

  private String getHeader(HttpServletRequest request, String key) {
    return request.getHeader(key);
  }
}
