package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSUserDetails;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class BPSApproval {

  private final String approvalId;
  private final BPSApprovalRequestType requestType;
  private final List<String> participantIds;
  private final ZonedDateTime date;
  private final BPSUserDetails requestedBy;
  private final BPSApprovalStatus status;
  private final BPSUserDetails approvedBy;
  private final ZonedDateTime approvedAt;
  private final String requestComment;
  private final BPSUserDetails rejectedBy;
  private final ZonedDateTime rejectedAt;
  private final Map<String, Object> originalData;
  private final Map<String, Object> requestedChange;
  private final String oldData;
  private final String newData;
  private final String notes;

  @JsonCreator
  public BPSApproval(
      @JsonProperty(value = "approvalId", required = true) String approvalId,
      @JsonProperty(value = "requestType") BPSApprovalRequestType requestType,
      @JsonProperty(value = "participantIds", required = true) List<String> participantIds,
      @JsonProperty(value = "date") ZonedDateTime date,
      @JsonProperty(value = "requestedBy", required = true) BPSUserDetails requestedBy,
      @JsonProperty(value = "status") BPSApprovalStatus status,
      @JsonProperty(value = "approvedBy") BPSUserDetails approvedBy,
      @JsonProperty(value = "approvedAt") ZonedDateTime approvedAt,
      @JsonProperty(value = "requestComment", required = true) String requestComment,
      @JsonProperty(value = "rejectedBy") BPSUserDetails rejectedBy,
      @JsonProperty(value = "rejectedAt") ZonedDateTime rejectedAt,
      @JsonProperty(value = "originalData", required = true) Map<String, Object> originalData,
      @JsonProperty(value = "requestedChange", required = true) Map<String, Object> requestedChange,
      @JsonProperty(value = "oldData", required = true) String oldData,
      @JsonProperty(value = "newData", required = true) String newData,
      @JsonProperty(value = "notes") String notes) {

    this.approvalId = approvalId;
    this.requestType = requestType;
    this.participantIds = participantIds;
    this.date = date;
    this.requestedBy = requestedBy;
    this.status = status;
    this.approvedBy = approvedBy;
    this.approvedAt = approvedAt;
    this.requestComment = requestComment;
    this.rejectedBy = rejectedBy;
    this.rejectedAt = rejectedAt;
    this.originalData = originalData;
    this.requestedChange = requestedChange;
    this.oldData = oldData;
    this.newData = newData;
    this.notes = notes;
  }
}
