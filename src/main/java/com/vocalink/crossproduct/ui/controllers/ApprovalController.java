package com.vocalink.crossproduct.ui.controllers;

import com.vocalink.crossproduct.ui.controllers.api.ApprovalApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationResponseDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.facade.api.ApprovalFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApprovalController implements ApprovalApi {

  private final ApprovalFacade approvalFacade;

  @PostMapping(value = "/approvals/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApprovalConfirmationResponseDto> submitApprovalConfirmation(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      @PathVariable final String id,
      @RequestBody final ApprovalConfirmationRequest request) {
    final ApprovalConfirmationResponseDto approvalDetailsDto = approvalFacade
        .submitApprovalConfirmation(context, clientType, request, id);
    return ResponseEntity.ok().body(approvalDetailsDto);
  }

  @GetMapping(value = "/approvals/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApprovalDetailsDto> getApprovalDetailsById(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      @PathVariable final String id) {
    final ApprovalDetailsDto approvalDetailsDto = approvalFacade
        .getApprovalDetailsById(context, clientType, id);
    return ResponseEntity.ok().body(approvalDetailsDto);
  }

  @GetMapping(value = "/approvals", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<ApprovalDetailsDto>> getApprovals(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      final ApprovalSearchRequest request) {

    PageDto<ApprovalDetailsDto> approvalDetailsDto = approvalFacade
        .getApprovals(context, clientType, request);

    return ResponseEntity.ok().body(approvalDetailsDto);
  }

  @PostMapping(value = "/approvals", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApprovalDetailsDto> requestApproval(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      @Valid @RequestBody final ApprovalChangeRequest request) {

    final ApprovalDetailsDto approvalDetailsDto = approvalFacade
        .requestApproval(context, clientType, request);

    return ResponseEntity.ok().body(approvalDetailsDto);
  }
}
