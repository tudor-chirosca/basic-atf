package com.vocalink.crossproduct.ui.controllers;

import java.io.IOException;

import static com.vocalink.crossproduct.ui.aspects.EventType.DOWNLOAD_REPORT;
import static com.vocalink.crossproduct.ui.aspects.EventType.VIEW_REPORTS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.controllers.api.ReportApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.facade.api.ReportFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReportController implements ReportApi {

  private final ReportFacade reportFacade;

  @Auditable(type = VIEW_REPORTS, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/reports", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<ReportDto>> getPaginatedReports(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      final ReportsSearchRequest searchParameters,
      final HttpServletRequest request) {
    log.debug("Request parameters: {}", searchParameters);

    final PageDto<ReportDto> paginated = reportFacade
        .getPaginated(context, clientType, searchParameters);

    return ResponseEntity.ok(paginated);
  }

  @Auditable(type = DOWNLOAD_REPORT, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/reports/{id}", produces = {APPLICATION_OCTET_STREAM_VALUE})
  public void getReport(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String id,
      final HttpServletRequest request,
      final HttpServletResponse response) throws IOException {

    reportFacade.writeReportToOutputStream(context, clientType, id, response.getOutputStream());

    response.addHeader(CONTENT_TYPE, APPLICATION_OCTET_STREAM_VALUE);
    response.addHeader(CONTENT_DISPOSITION, "attachment;filename=" + id);
    response.setStatus(HttpStatus.OK.value());
  }
}
