package com.vocalink.crossproduct.ui.controllers.api;

import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationResponse;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationResponseDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.dto.participant.ApprovalUserDto;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ApprovalApi {

  @ApiOperation("Fetch Approval details by id")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Approval details fetched successfully", response = ApprovalDetailsDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<ApprovalDetailsDto> getApprovalDetailsById(final ClientType clientType,
      final String context, final String id);

  @ApiOperation("Request approve/reject")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Request approved/rejected successfully", response = ApprovalConfirmationResponse.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<ApprovalConfirmationResponseDto> submitApprovalConfirmation(
      final ClientType clientType, final String context, final String id,
      @Valid final ApprovalConfirmationRequest request,
      final HttpServletRequest httpServletRequest);

  @ApiOperation("Fetch all Approvals")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Approvals fetched successfully", response = PageDto.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<PageDto<ApprovalDetailsDto>> getApprovals(final ClientType clientType,
      final String context,
      @Valid final ApprovalSearchRequest request,
      final HttpServletRequest httpServletRequest);

  @ApiOperation("Create Approval request")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Create approval Request successfully", response = ApprovalConfirmationResponse.class),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<ApprovalDetailsDto> requestApproval(
      final ClientType clientType,
      final String context,
      @Valid final ApprovalChangeRequest request,
      final HttpServletRequest httpServletRequest);

  @ApiOperation("Fetch Request by users references")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Request by users references successfully", response = ApprovalUserDto.class, responseContainer = "List"),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<List<ApprovalUserDto>> findRequestedDetails(
      final ClientType clientType, final String context);

  @ApiOperation("Fetch Approval request types references")
  @ApiResponses({
      @ApiResponse(code = 200, message = "Approval request types references successfully", response = ApprovalRequestType.class, responseContainer = "List"),
      @ApiResponse(code = 400, message = "Some of the request params are invalid")
  })
  ResponseEntity<List<ApprovalRequestType>> findApprovalRequestTypes(
      final ClientType clientType, final String context);
}
