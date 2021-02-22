package com.vocalink.crossproduct.ui.controllers;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;

import com.vocalink.crossproduct.ui.controllers.api.ReportApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.facade.api.ReportFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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

  @GetMapping(value = "/reports", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<ReportDto>> getPaginatedReports(
      @RequestHeader("client-type") final ClientType clientType, @RequestHeader final String context,
      final ReportsSearchRequest searchParameters) {
    log.debug("Request parameters: {}", searchParameters);

    final PageDto<ReportDto> paginated = reportFacade
        .getPaginated(context, clientType, searchParameters);

    return ResponseEntity.ok(paginated);
  }

  @GetMapping(value = "/reports/{id}",
      produces = {APPLICATION_OCTET_STREAM_VALUE})
  public ResponseEntity<?> getReport(
      final @RequestHeader("client-type") ClientType clientType,
      final @RequestHeader String context,
      final @PathVariable String id) {

    Resource resource = reportFacade.getReport(context, clientType, id);

    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(CONTENT_TYPE, APPLICATION_OCTET_STREAM_VALUE);
    httpHeaders.add(CONTENT_DISPOSITION, "attachment;filename=" + id);

    return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
  }
}
