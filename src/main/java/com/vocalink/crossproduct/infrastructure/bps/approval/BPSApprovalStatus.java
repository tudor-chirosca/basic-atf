package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum BPSApprovalStatus {

  @JsonProperty("WAITING-FORAPPROVAL")
  PENDING,
  @JsonProperty("APPROVED")
  APPROVED,
  @JsonProperty("REJECTED")
  REJECTED
}
