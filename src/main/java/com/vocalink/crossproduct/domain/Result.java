package com.vocalink.crossproduct.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Result<T> {

  private final List<T> data;
  private final Result.ResultSummary summary;

  @Getter
  @AllArgsConstructor
  public static class ResultSummary {

    private final int pageSize;
    private final int offset;
    private final int totalCount;

    public static ResultSummary empty() {
      return new ResultSummary(0, 0, 0);
    }
  }
}
