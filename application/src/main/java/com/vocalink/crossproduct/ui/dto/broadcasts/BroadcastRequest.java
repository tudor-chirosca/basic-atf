package com.vocalink.crossproduct.ui.dto.broadcasts;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BroadcastRequest {

  private final String message;
  private final List<String> recipients;

  @JsonCreator
  public BroadcastRequest(
      @JsonProperty(value = "message", required = true) String message,
      @JsonProperty(value = "recipients", required = true) List<String> recipients) {
    this.message = message;
    this.recipients = recipients;
  }

  @JsonProperty("broadcastForAll")
  private Boolean broadcastForAll(){
    return !isNull(recipients) && recipients.isEmpty();
  }
}
