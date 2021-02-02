package com.vocalink.crossproduct.infrastructure.bps.broadcasts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSBroadcast {

  private final ZonedDateTime createdAt;
  private final String broadcastId;
  private final String message;
  private final List<String> recipients;

  @JsonCreator
  public BPSBroadcast(@JsonProperty(value = "createdAt", required = true) ZonedDateTime createdAt,
      @JsonProperty(value = "broadcastId", required = true) String broadcastId,
      @JsonProperty(value = "msg", required = true) String message,
      @JsonProperty(value = "recipients", required = true) List<String> recipients) {
    this.createdAt = createdAt;
    this.broadcastId = broadcastId;
    this.message = message;
    this.recipients = recipients;
  }
}
