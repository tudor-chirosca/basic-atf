package com.vocalink.crossproduct.ui.dto.approval;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalStatus;
import com.vocalink.crossproduct.domain.approval.ApprovalUser;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalDetailsDto {

  private final ApprovalStatus status;
  private final ApprovalUser requestedBy;
  @JsonInclude(Include.NON_EMPTY)
  private final ApprovalUser approvedBy;
  private final ZonedDateTime createdAt;
  private final String jobId;
  private final ApprovalRequestType requestType;
  private final String participantIdentifier;
  private final String participantName;
  private final String requestComment;
  @JsonInclude(Include.NON_EMPTY)
  private final ApprovalUser rejectedBy;
  @JsonInclude(Include.NON_EMPTY)
  private final String notes;
  private final Map<String, String> originalData;
  private final Map<String, String> requestedChange;
}
