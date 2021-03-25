package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
  private final BPSApprovalUser requestedBy;
  private final BPSApprovalStatus status;
  private final BPSApprovalUser approvedBy;
  private final String requestComment;
  private final BPSApprovalUser rejectedBy;
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
      @JsonProperty(value = "requestedBy", required = true) BPSApprovalUser requestedBy,
      @JsonProperty(value = "status") BPSApprovalStatus status,
      @JsonProperty(value = "approvedBy") BPSApprovalUser approvedBy,
      @JsonProperty(value = "requestComment", required = true) String requestComment,
      @JsonProperty(value = "rejectedBy") BPSApprovalUser rejectedBy,
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
    this.requestComment = requestComment;
    this.rejectedBy = rejectedBy;
    this.originalData = originalData;
    this.requestedChange = requestedChange;
    this.oldData = oldData;
    this.newData = newData;
    this.notes = notes;
  }
}
