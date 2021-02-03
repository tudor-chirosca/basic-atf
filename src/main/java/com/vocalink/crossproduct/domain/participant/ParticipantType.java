package com.vocalink.crossproduct.domain.participant;

import lombok.Getter;

@Getter
public enum ParticipantType {
  DIRECT("DIRECT"),
  FUNDING("FUNDING"),
  FUNDED("FUNDED"),
  DIRECT_FUNDING("DIRECT+FUNDING"),
  INDIRECT("INDIRECT"),
  TPSP("TPSP"),
  SCHEME_OPERATOR("SCHEME");

  private final String description;

  ParticipantType(String description) {
    this.description = description;
  }
}
