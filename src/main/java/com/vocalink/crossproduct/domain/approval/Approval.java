package com.vocalink.crossproduct.domain.approval;

import java.time.ZonedDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Approval {

  private final String approvalId;
  private final ApprovalRequestType requestType;
  private final String schemeParticipantIdentifier;
  private final ZonedDateTime date;
  private final ApprovalUser requestedBy;
  private final ApprovalStatus status;
  private final ApprovalUser approvedBy;
  private final String participantName;
  private final String requestComment;
  private final ApprovalUser rejectedBy;
  private final Map<String, Object> originalData;
  private final Map<String, Object> requestedChange;
  private final String oldData;
  private final String newData;
  private final String notes;
}
