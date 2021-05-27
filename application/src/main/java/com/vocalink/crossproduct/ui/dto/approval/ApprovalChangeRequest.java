package com.vocalink.crossproduct.ui.dto.approval;

import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.BATCH_CANCELLATION;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.ui.aspects.AuditableRequest;
import com.vocalink.crossproduct.ui.aspects.EventType;
import com.vocalink.crossproduct.ui.validations.ValidChangeRequest;
import com.vocalink.crossproduct.ui.validations.ValidEnum;
import com.vocalink.crossproduct.ui.validations.ValidNote;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
@ValidNote(notes = "notes", requestType = "requestType", requestTypes = {"BATCH_CANCELLATION"})
public class ApprovalChangeRequest implements AuditableRequest {

  @ValidEnum(enumClass = ApprovalRequestType.class)
  private final String requestType;
  @ValidChangeRequest
  private final Map<String, Object> requestedChange;
  private final String notes;

  @JsonCreator
  public ApprovalChangeRequest(
      @JsonProperty(value = "requestType", required = true) String requestType,
      @JsonProperty(value = "requestedChange", required = true) Map<String, Object> requestedChange,
      @JsonProperty(value = "notes") String notes) {
    this.requestType = requestType.toUpperCase();
    this.requestedChange = requestedChange;
    this.notes = notes;
  }

  @Override
  public EventType getEventType() {
    return ApprovalRequestType.valueOf(requestType).getEventType();
  }

  @Override
  public Map<String, String> getAuditableContent() {
    final Map<String, String> content = new HashMap<>();
    if (requestType.equals(BATCH_CANCELLATION.toString())) {
      content.put("batchId", requestedChange.getOrDefault("batchId", EMPTY).toString());
    } else {
      content.put("id", requestedChange.getOrDefault("id", EMPTY).toString());
    }
    content.put("notes", notes);
    return content;
  }
}
