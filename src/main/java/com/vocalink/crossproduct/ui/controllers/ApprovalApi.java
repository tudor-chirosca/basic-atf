package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface ApprovalApi {

  @ApiOperation("Fetch Approval details by id")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Approval details fetched successfully", response = ApprovalDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<ApprovalDetailsDto> getApprovalDetailsById(ClientType clientType, String context,
      String id);

  @ApiOperation("Fetch all Approvals")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Approvals fetched successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<ApprovalDetailsDto>> getApprovals(final ClientType clientType, final String context,
      final ApprovalSearchRequest request);
}
