package com.vocalink.crossproduct.infrastructure.bps.broadcasts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSBroadcastsMessageRequest {

  private final String message;
  private final List<String> recipients;

  @JsonCreator
  public BPSBroadcastsMessageRequest(
      @JsonProperty(value = "message", required = true) String message,
      @JsonProperty(value = "recipients", required = true) List<String> recipients) {
    this.message = message;
    this.recipients = recipients;
  }
}
