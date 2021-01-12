package com.vocalink.crossproduct.infrastructure.bps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSPage<T> {

  private final int totalResults;
  private final List<T> items;

  @JsonCreator
  public BPSPage(
      @JsonProperty(value = "totalResults") final int totalResults,
      @JsonProperty(value = "items") final List<T> items) {
    this.totalResults = totalResults;
    this.items = items;
  }
}
