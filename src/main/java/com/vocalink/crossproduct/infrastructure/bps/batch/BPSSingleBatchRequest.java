package com.vocalink.crossproduct.infrastructure.bps.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSSingleBatchRequest {

  private final String batchId;
}
