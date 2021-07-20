package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSApprovalCreationResponse {

  private final String requestId;
  private final String status;

  public BPSApprovalCreationResponse(
      @JsonProperty(value = "requestId") String requestId,
      @JsonProperty(value = "status") String status) {
    this.requestId = requestId;
    this.status = status;
  }
}
