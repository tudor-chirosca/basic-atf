package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordDto;
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface RoutingRecordsApi {

  @ApiOperation("Fetch all routing records")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Routing records retrieved successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<RoutingRecordDto>> getRoutingRecords(final ClientType clientType,
      final String context, final String bic, final RoutingRecordRequest request);

}
