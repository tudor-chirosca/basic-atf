package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDetailsDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface BatchesApi {

  @ApiOperation("Fetch Batches")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Batches fetched successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<BatchDto>> getBatches(
      ClientType clientType,
      String context,
      @Valid BatchEnquirySearchRequest searchRequest,
      HttpServletRequest request
  );

  @ApiOperation("Fetch Batch Details")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Batch details fetched successfully", response = BatchDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<BatchDetailsDto> getBatchDetails(
      ClientType clientType,
      String context,
      String batchId,
      HttpServletRequest request);
}
