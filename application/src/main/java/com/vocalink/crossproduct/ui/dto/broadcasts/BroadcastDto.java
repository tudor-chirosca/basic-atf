package com.vocalink.crossproduct.ui.dto.broadcasts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BroadcastDto {

  private final ZonedDateTime createdAt;
  private final String broadcastId;
  private final String message;
  @Setter
  private List<ParticipantReferenceDto> recipients;

  @JsonCreator
  public BroadcastDto(
      @JsonProperty(value = "createdAt", required = true) ZonedDateTime createdAt,
      @JsonProperty(value = "broadcastId", required = true) String broadcastId,
      @JsonProperty(value = "message", required = true) String message,
      @JsonProperty(value = "recipients", required = true) List<ParticipantReferenceDto> recipients) {
    this.createdAt = createdAt;
    this.broadcastId = broadcastId;
    this.message = message;
    this.recipients = recipients;
  }
}
