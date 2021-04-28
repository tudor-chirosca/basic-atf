package com.vocalink.crossproduct.ui.dto.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType;
import lombok.Getter;

@Getter
public class ApprovalConfirmationRequest {

  private final ApprovalConfirmationType action;
  private final String message;

  @JsonCreator
  public ApprovalConfirmationRequest(
      @JsonProperty(value = "action", required = true) ApprovalConfirmationType action,
      @JsonProperty(value = "message") String message) {
    this.action = action;
    this.message = message;
  }
}
