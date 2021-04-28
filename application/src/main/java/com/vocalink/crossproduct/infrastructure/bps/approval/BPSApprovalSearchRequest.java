package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSApprovalSearchRequest {

  private final String approvalId;
  private final ZonedDateTime fromDate;
  private final ZonedDateTime toDate;
  private final List<String> schemeParticipantIdentifiers;
  private final List<BPSApprovalRequestType> requestTypes;
  private final List<String> requestedBy;
  private final List<BPSApprovalStatus> statuses;
  private final List<BPSSortingQuery> sortingOrder;
}
