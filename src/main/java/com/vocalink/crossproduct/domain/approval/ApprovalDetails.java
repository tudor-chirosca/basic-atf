package com.vocalink.crossproduct.domain.approval;

import java.time.ZonedDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalDetails {

  private final ApprovalStatus status;
  private final ApprovalUser requestedBy;
  private final ApprovalUser approvedBy;
  private final ZonedDateTime createdAt;
  private final String jobId;
  private final ApprovalRequestType requestType;
  private final String participantIdentifier;
  private final String participantName;
  private final RejectionReason rejectionReason;
  private final Map<String, String> requestedChange;
}
