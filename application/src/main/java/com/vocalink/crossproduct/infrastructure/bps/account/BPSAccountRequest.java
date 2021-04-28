package com.vocalink.crossproduct.infrastructure.bps.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSAccountRequest {

  private final String partyCode;

  @JsonCreator
  public BPSAccountRequest(@JsonProperty(value = "partyCode") String partyCode) {
    this.partyCode = partyCode;
  }
}
