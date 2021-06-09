package com.vocalink.crossproduct.ui.dto.approval;

import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.BATCH_CANCELLATION;
import static com.vocalink.crossproduct.domain.approval.ApprovalRequestType.CONFIG_CHANGE;
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
  public Map<String, Object> getAuditableContent() {
    final Map<String, Object> content = new HashMap<>();
    content.put("notes", notes);
    if (CONFIG_CHANGE.toString().equals(requestType)) {
      Map<String, Object> requestedValues = new HashMap<>();
      requestedValues.put("id", requestedChange.getOrDefault("id", ""));
      requestedValues.put("name", requestedChange.getOrDefault("name", ""));
      requestedValues.put("settlementAccountNo", requestedChange.getOrDefault("settlementAccountNo", ""));
      requestedValues.put("debitCapLimit", requestedChange.getOrDefault("debitCapLimit", ""));
      requestedValues.put("debitCapLimitThresholds", requestedChange.getOrDefault("debitCapLimitThresholds", ""));
      requestedValues.put("outputTxnVolume", requestedChange.getOrDefault("outputTxnVolume", ""));
      requestedValues.put("outputTxnTimeLimit", requestedChange.getOrDefault("outputTxnTimeLimit", ""));
      content.put("requestedValues", requestedValues);
      return content;
    }
    if (BATCH_CANCELLATION.toString().equals(requestType)) {
      content.put("batchId", requestedChange.getOrDefault("batchId", EMPTY).toString());
      return content;
    }
    content.put("id", requestedChange.getOrDefault("id", EMPTY).toString());
    return content;
  }
}
