package com.vocalink.crossproduct.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IORejectedStats {
  private String datetime;
  private double filesRejected;
  private double batchesRejected;
  private double transactionsRejected;
}
