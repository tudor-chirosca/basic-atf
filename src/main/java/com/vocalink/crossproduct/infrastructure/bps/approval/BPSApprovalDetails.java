package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.Getter;

@Getter
public class BPSApprovalDetails {

  private final String status;
  private final BPSApprovalUser requestedBy;
  private final BPSApprovalUser approvedBy;
  private final ZonedDateTime createdAt;
  private final String jobId;
  private final String requestType;
  private final String participantIdentifier;
  private final String participantName;
  private final BPSRejectionReason rejectionReason;
  private final Map<String, String> requestedChange;

  @JsonCreator
  public BPSApprovalDetails(
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "requestedBy", required = true) BPSApprovalUser requestedBy,
      @JsonProperty(value = "approvedBy") BPSApprovalUser approvedBy,
      @JsonProperty(value = "createdAt", required = true) ZonedDateTime createdAt,
      @JsonProperty(value = "jobId", required = true) String jobId,
      @JsonProperty(value = "requestType", required = true) String requestType,
      @JsonProperty(value = "participantIdentifier", required = true) String participantIdentifier,
      @JsonProperty(value = "participantName", required = true) String participantName,
      @JsonProperty(value = "rejectionReason") BPSRejectionReason rejectionReason,
      @JsonProperty(value = "requestedChange", required = true) Map<String, String> requestedChange) {
    this.status = status;
    this.requestedBy = requestedBy;
    this.approvedBy = approvedBy;
    this.createdAt = createdAt;
    this.jobId = jobId;
    this.requestType = requestType;
    this.participantIdentifier = participantIdentifier;
    this.participantName = participantName;
    this.rejectionReason = rejectionReason;
    this.requestedChange = requestedChange;
  }
}
