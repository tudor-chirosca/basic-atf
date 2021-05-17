package com.vocalink.crossproduct.ui.controllers.api;

import java.io.IOException;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ReportApi {

  @ApiOperation("Fetch Report")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Reports fetched successfully", response = ReportDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<ReportDto>> getPaginatedReports(
      final ClientType clientType,
      final String context,
      @Valid final ReportsSearchRequest searchParameters,
      final HttpServletRequest request);

  @ApiOperation("Fetch Report Details")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Report details fetched successfully", response = ReportDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  void getReport(final ClientType clientType, final String context, final String id,
      final HttpServletRequest request, final HttpServletResponse response) throws IOException;
}
