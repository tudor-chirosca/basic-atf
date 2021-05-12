package com.vocalink.crossproduct.ui.controllers;

import static com.vocalink.crossproduct.ui.aspects.EventType.VIEW_PTT_IO_DASHBOARD;
import static com.vocalink.crossproduct.ui.aspects.EventType.VIEW_SCHEME_IO_DASHBOARD;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.InputOutputApi;
import com.vocalink.crossproduct.ui.dto.IODashboardDto;
import com.vocalink.crossproduct.ui.dto.io.IODetailsDto;
import com.vocalink.crossproduct.ui.facade.api.InputOutputFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
public class InputOutputController implements InputOutputApi {

  private final InputOutputFacade inputOutputFacade;

  @Auditable(type = VIEW_SCHEME_IO_DASHBOARD, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/io", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IODashboardDto> getSettlement(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      @RequestParam(required = false) final String timestamp,
      final HttpServletRequest httpServletRequest) {

    LocalDate dateFrom = getDate(timestamp);

    IODashboardDto settlementDto = inputOutputFacade
        .getInputOutputDashboard(context.toUpperCase(), clientType, dateFrom);

    return ResponseEntity.ok().body(settlementDto);
  }

  @Auditable(type = VIEW_PTT_IO_DASHBOARD, params = @Positions(clientType = 0, context = 1, content = 2, request = 4))
  @GetMapping(value = "/io/{participantId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<IODetailsDto> getIODetails(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      @PathVariable final String participantId,
      @RequestParam(required = false) final String forDate,
      final HttpServletRequest httpServletRequest) {

    LocalDate dateFrom = getDate(forDate);

    IODetailsDto ioDetails = inputOutputFacade
        .getInputOutputDetails(context.toUpperCase(), clientType, participantId, dateFrom);

    return ResponseEntity.ok().body(ioDetails);
  }

  private LocalDate getDate(String timestamp) {
    return StringUtils.isEmpty(timestamp)
        ? LocalDate.now()
        : LocalDate.parse(timestamp);
  }
}
