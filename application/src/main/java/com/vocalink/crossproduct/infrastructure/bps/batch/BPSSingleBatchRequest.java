package com.vocalink.crossproduct.infrastructure.bps.batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSSingleBatchRequest {

  @JsonInclude(Include.NON_EMPTY)
  private final String instructionId;
  private final String messageIdentifier;
}
