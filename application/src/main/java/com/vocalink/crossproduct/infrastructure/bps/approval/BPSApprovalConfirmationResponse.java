package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSApprovalConfirmationResponse {

  private final String responseMessage;

  public BPSApprovalConfirmationResponse(
      @JsonProperty(value = "responseMessage") String responseMessage) {
    this.responseMessage = responseMessage;
  }
}
