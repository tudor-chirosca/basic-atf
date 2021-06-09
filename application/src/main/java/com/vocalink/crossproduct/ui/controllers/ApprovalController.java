package com.vocalink.crossproduct.ui.controllers;

import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.CONFIG_CHANGE;
import static com.vocalink.crossproduct.ui.aspects.EventType.VIEW_APPROVAL_DASHBOARD;

import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.ui.aspects.Auditable;
import com.vocalink.crossproduct.ui.aspects.Positions;
import com.vocalink.crossproduct.ui.aspects.AuditableDetail;
import com.vocalink.crossproduct.ui.controllers.api.ApprovalApi;
import com.vocalink.crossproduct.ui.dto.PageDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalChangeRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalConfirmationResponseDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalDetailsDto;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.dto.participant.ApprovalUserDto;
import com.vocalink.crossproduct.ui.facade.api.ApprovalFacade;
import com.vocalink.crossproduct.ui.presenter.ClientType;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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

  private final AuditableDetail auditableDetail;

  private final ApprovalFacade approvalFacade;

  @Auditable(params = @Positions(clientType = 0, context = 1, content = 3, request = 4, requestId = 2))
  @PostMapping(value = "/approvals/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApprovalConfirmationResponseDto> submitApprovalConfirmation(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      @PathVariable final String id,
      @RequestBody final ApprovalConfirmationRequest request,
      final HttpServletRequest httpServletRequest) {
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

  @Auditable(type = VIEW_APPROVAL_DASHBOARD, params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @GetMapping(value = "/approvals", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PageDto<ApprovalDetailsDto>> getApprovals(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      final ApprovalSearchRequest request,
      final HttpServletRequest httpServletRequest) {

    final PageDto<ApprovalDetailsDto> approvalDetailsDto = approvalFacade
        .getApprovals(context, clientType, request);

    return ResponseEntity.ok().body(approvalDetailsDto);
  }

  @Auditable(params = @Positions(clientType = 0, context = 1, content = 2, request = 3))
  @PostMapping(value = "/approvals", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApprovalDetailsDto> requestApproval(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context,
      @Valid @RequestBody final ApprovalChangeRequest request,
      final HttpServletRequest httpServletRequest) {

    final ApprovalDetailsDto approvalDetailsDto = approvalFacade
        .requestApproval(context, clientType, request);

    auditableDetail.setJobId(approvalDetailsDto.getJobId());

    if(CONFIG_CHANGE.toString().equals(request.getRequestType())) {
      auditableDetail.setPreviousValues(approvalDetailsDto.getOriginalData());
    }
    return ResponseEntity.ok().body(approvalDetailsDto);
  }

  @GetMapping(value = "/reference/approvals/request-by", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ApprovalUserDto>> findRequestedDetails(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context) {

    final List<ApprovalUserDto> approvalUsersDto = approvalFacade
        .findRequestedDetails(context, clientType);

    return ResponseEntity.ok().body(approvalUsersDto);
  }

  @GetMapping(value = "/reference/approvals/request-types", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ApprovalRequestType>> findApprovalRequestTypes(
      @RequestHeader("client-type") final ClientType clientType,
      @RequestHeader final String context) {

    final List<ApprovalRequestType> approvalRequestTypes = approvalFacade
        .findApprovalRequestTypes(context, clientType);

    return ResponseEntity.ok().body(approvalRequestTypes);
  }
}
