package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchDto;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface BatchesApi {

  @ApiOperation("Fetch Batches")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Batches fetched successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<BatchDto>> getBatches(
      final ClientType clientType,
      final String context,
      final BatchEnquirySearchRequest request
  );

}
