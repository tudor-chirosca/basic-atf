package com.vocalink.crossproduct.infrastructure.bps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSResult<T> {

  private final List<T> data;
  private final BPSResultSummary summary;

  @JsonCreator
  public BPSResult(
      @JsonProperty(value = "data") List<T> data,
      @JsonProperty(value = "summary") BPSResultSummary summary) {
    this.data = data;
    this.summary = summary;
  }

  @Getter
  public static class BPSResultSummary {

    private final int pageSize;
    private final int offset;
    private final int totalCount;

    @JsonCreator
    public BPSResultSummary(
        @JsonProperty(value = "pageSize", required = true) int pageSize,
        @JsonProperty(value = "offset", required = true) int offset,
        @JsonProperty(value = "totalCount", required = true) int totalCount) {
      this.pageSize = pageSize;
      this.offset = offset;
      this.totalCount = totalCount;
    }
  }
}
