package com.vocalink.crossproduct.ui.dto.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.domain.approval.ApprovalConfirmationType;
import com.vocalink.crossproduct.ui.aspects.AuditableRequest;
import com.vocalink.crossproduct.ui.aspects.EventType;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonInclude(Include.NON_NULL)
public class ApprovalConfirmationRequest implements AuditableRequest {

  private final ApprovalConfirmationType action;
  private final String message;

  @JsonCreator
  public ApprovalConfirmationRequest(
      @JsonProperty(value = "action", required = true) ApprovalConfirmationType action,
      @JsonProperty(value = "message") String message) {
    this.action = action;
    this.message = message;
  }

  @Override
  public EventType getEventType() {
    return action.getEventType();
  }

  @Override
  public Map<String, Object> getAuditableContent() {
    final Map<String, Object> content = new HashMap<>();
    content.put("message", message);
    return content;
  }
}
