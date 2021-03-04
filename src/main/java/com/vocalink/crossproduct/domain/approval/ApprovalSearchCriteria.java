package com.vocalink.crossproduct.domain.approval;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalSearchCriteria {

  private final int offset;
  private final int limit;
  private final String jobId;
  private final ZonedDateTime fromDate;
  private final ZonedDateTime toDate;
  private final List<String> participantIds;
  private final List<ApprovalRequestType> requestTypes;
  private final String requestedBy;
  private final List<ApprovalStatus> statuses;
  private final List<String> sort;
}
