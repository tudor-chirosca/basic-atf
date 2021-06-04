package com.vocalink.crossproduct.infrastructure.bps.broadcasts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Collections.emptyList;

import lombok.Getter;

@Getter
public class BPSBroadcast {

  private final ZonedDateTime createdAt;
  private final String broadcastId;
  private final String message;
  private final List<String> recipients;

  @JsonCreator
  public BPSBroadcast(@JsonProperty(value = "createdAt") ZonedDateTime createdAt,
      @JsonProperty(value = "broadcastId") String broadcastId,
      @JsonProperty(value = "msg") String message,
      @JsonProperty(value = "recipients") List<String> recipients) {
    this.createdAt = createdAt;
    this.broadcastId = broadcastId;
    this.message = message;
    this.recipients = recipients == null ? emptyList() : recipients;
  }
}
