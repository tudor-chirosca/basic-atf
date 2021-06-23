package com.vocalink.crossproduct.infrastructure.bps.reference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BPSMessageDirectionReference {

  private final String messageType;
  private final String description;
  private final String formatName;
  private final List<String> messageDirection;
  private final List<String> level;
  private final List<String> type;

  @JsonCreator
  public BPSMessageDirectionReference(
      @JsonProperty(value = "messageType", required = true) String messageType,
      @JsonProperty(value = "description") String description,
      @JsonProperty(value = "formatName") String formatName,
      @JsonProperty(value = "messageDirection", required = true) List<String> messageDirection,
      @JsonProperty(value = "level", required = true) List<String> level,
      @JsonProperty(value = "type", required = true) List<String> type) {
    this.messageType = messageType;
    this.description = description;
    this.formatName = formatName;
    this.messageDirection = messageDirection;
    this.level = level;
    this.type = type;
  }
}
