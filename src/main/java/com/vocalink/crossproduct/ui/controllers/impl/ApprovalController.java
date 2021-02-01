package com.vocalink.crossproduct.ui.controllers.impl;

import com.vocalink.crossproduct.ui.controllers.ApprovalApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.facade.ApprovalFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApprovalController implements ApprovalApi {

  private final ApprovalFacade approvalFacade;

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
}
