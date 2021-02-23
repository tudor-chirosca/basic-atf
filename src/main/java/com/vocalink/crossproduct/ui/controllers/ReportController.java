package com.vocalink.crossproduct.ui.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.vocalink.crossproduct.ui.controllers.api.ReportApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.facade.api.ReportFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReportController implements ReportApi {

  private final ReportFacade reportFacade;

  @GetMapping(value = "/reports", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<ReportDto>> getPaginatedReports(
      @RequestHeader("client-type") final ClientType clientType, @RequestHeader final String context,
      final ReportsSearchRequest searchParameters) {
    log.debug("Request parameters: {}", searchParameters);

    final PageDto<ReportDto> paginated = reportFacade
        .getPaginated(context, clientType, searchParameters);

    return ResponseEntity.ok(paginated);
  }
}
