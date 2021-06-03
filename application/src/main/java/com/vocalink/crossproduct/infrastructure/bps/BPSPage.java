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
      @JsonProperty(value = "totalResults") final Integer totalResults,
      @JsonProperty(value = "items") final List<T> items) {
    this.totalResults = totalResults == null ? 0 : totalResults;
    this.items = items;
  }
}
