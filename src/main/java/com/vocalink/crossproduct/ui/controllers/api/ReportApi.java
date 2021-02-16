package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.report.ReportDto;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ReportApi {

  @ApiOperation("Fetch Report")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Reports fetched successfully", response = ReportDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<ReportDto>> getPaginatedReports(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      ReportsSearchRequest searchParameters);
}
