package com.vocalink.crossproduct.infrastructure.bps.cycle;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BPSCycleStatus {
  OPEN,
  CLOSED,
  COMPLETED,
  @JsonProperty("PARTIALLYCOMPLETE")
  PARTIALLY_COMPLETE,
  NOT_STARTED,
  SETTLING,
  NO_RESPONSE
}
