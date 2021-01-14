package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSIOBatchesMessageTypes {

  private final String name;
  private final String code;
  private final BPSIODataDetails data;

  @JsonCreator
  public BPSIOBatchesMessageTypes(
      @JsonProperty(value = "name") String name,
      @JsonProperty(value = "code") String code,
      @JsonProperty(value = "data") BPSIODataDetails data) {
    this.name = name;
    this.code = code;
    this.data = data;
  }
}
