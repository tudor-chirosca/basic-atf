package com.vocalink.crossproduct.domain.participant;

import lombok.Getter;

@Getter
public enum ParticipantType {
  DIRECT_ONLY("DIRECT+ONLY"),
  FUNDING("FUNDING"),
  FUNDED("FUNDED"),
  DIRECT_FUNDING("DIRECT+FUNDING"),
  SCHEME("SCHEME");

  private final String description;

  ParticipantType(String description) {
    this.description = description;
  }
}
