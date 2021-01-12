package com.vocalink.crossproduct.infrastructure.bps.reference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSMessageDirectionReference {

  private final String name;
  private final List<String> types;

  @JsonCreator
  public BPSMessageDirectionReference(
      @JsonProperty(value = "name") String name,
      @JsonProperty(value = "types") List<String> types) {
    this.name = name;
    this.types = types;
  }
}
