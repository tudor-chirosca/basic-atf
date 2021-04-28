package com.vocalink.crossproduct.ui.dto.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.domain.approval.ApprovalRequestType;
import com.vocalink.crossproduct.ui.validations.ValidChangeRequest;
import com.vocalink.crossproduct.ui.validations.ValidEnum;
import com.vocalink.crossproduct.ui.validations.ValidNote;
import java.util.Map;
import lombok.Getter;

@Getter
@ValidNote(notes = "notes", requestType = "requestType", requestTypes = {"BATCH_CANCELLATION"})
public class ApprovalChangeRequest {

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
}
