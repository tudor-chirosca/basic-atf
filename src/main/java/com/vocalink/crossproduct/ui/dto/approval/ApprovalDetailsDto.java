package com.vocalink.crossproduct.ui.dto.approval;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.domain.approval.ApprovalStatus;
import com.vocalink.crossproduct.ui.dto.participant.ApprovalUserDto;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalDetailsDto {

  private final ApprovalStatus status;
  private final ApprovalUserDto requestedBy;
  @JsonInclude(Include.NON_EMPTY)
  private final ApprovalUserDto approvedBy;
  @JsonInclude(Include.NON_EMPTY)
  private final ZonedDateTime approvedAt;
  private final ZonedDateTime createdAt;
  private final String jobId;
  private final ApprovalRequestType requestType;
  private final List<ParticipantReferenceDto> participants;
  private final String requestComment;
  @JsonInclude(Include.NON_EMPTY)
  private final ApprovalUserDto rejectedBy;
  @JsonInclude(Include.NON_EMPTY)
  private final ZonedDateTime rejectedAt;
  @JsonInclude(Include.NON_EMPTY)
  private final String notes;
  private final Map<String, Object> originalData;
  private final Map<String, Object> requestedChange;
}
