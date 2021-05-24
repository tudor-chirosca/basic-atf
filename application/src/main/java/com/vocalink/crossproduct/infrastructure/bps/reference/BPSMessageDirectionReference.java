package com.vocalink.crossproduct.infrastructure.bps.reference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSMessageDirectionReference {

  private final String messageType;
  private final String description;
  private final String formatName;
  private final String messageDirection;

  @JsonCreator
  public BPSMessageDirectionReference(
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "description") String description,
      @JsonProperty(value = "formatName") String formatName,
      @JsonProperty(value = "messageDirection") String messageDirection) {
    this.messageType = messageType;
    this.description = description;
    this.formatName = formatName;
    this.messageDirection = messageDirection;
  }
}
