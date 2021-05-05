package com.vocalink.crossproduct.domain.approval;

import com.vocalink.crossproduct.domain.audit.UserDetails;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Approval {

  private final String approvalId;
  private final ApprovalRequestType requestType;
  private final List<String> participantIds;
  private final ZonedDateTime date;
  private final UserDetails requestedBy;
  private final ZonedDateTime approvedAt;
  private final ApprovalStatus status;
  private final UserDetails approvedBy;
  private final String requestComment;
  private final UserDetails rejectedBy;
  private final ZonedDateTime rejectedAt;
  private final Map<String, Object> originalData;
  private final Map<String, Object> requestedChange;
  private final String oldData;
  private final String newData;
  private final String notes;
}
