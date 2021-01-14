package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSIOTransactionsMessageTypes {

  private final String name;
  private final String code;
  private final BPSIODataAmountDetails data;

  @JsonCreator
  public BPSIOTransactionsMessageTypes(
      @JsonProperty(value = "name") String name,
      @JsonProperty(value = "code") String code,
      @JsonProperty(value = "data") BPSIODataAmountDetails data) {
    this.name = name;
    this.code = code;
    this.data = data;
  }
}
