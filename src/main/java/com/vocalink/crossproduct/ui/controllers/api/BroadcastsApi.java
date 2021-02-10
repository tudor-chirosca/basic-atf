package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastRequest;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastsSearchParameters;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

public interface BroadcastsApi {

  @ApiOperation("Fetch Broadcast")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Broadcast fetched successfully", response = BroadcastDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<BroadcastDto>> getPaginatedBroadcasts(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      BroadcastsSearchParameters searchParameters);

  @ApiOperation("Create new broadcast")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Broadcast created successfully"),
      @ApiResponse(code = 400, message = "Some of the request params are invalid", response = BroadcastDto.class)
  })
  ResponseEntity<BroadcastDto> sendBroadcast(
      @RequestHeader("client-type") ClientType clientType, @RequestHeader String context,
      BroadcastRequest request);
}
