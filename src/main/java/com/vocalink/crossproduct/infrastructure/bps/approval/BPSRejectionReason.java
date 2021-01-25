package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSRejectionReason {

  private final BPSApprovalUser rejectedBy;
  private final String comment;

  @JsonCreator
  public BPSRejectionReason(
      @JsonProperty(value = "rejectedBy", required = true) BPSApprovalUser rejectedBy,
      @JsonProperty(value = "comment", required = true) String comment) {
    this.rejectedBy = rejectedBy;
    this.comment = comment;
  }
}
