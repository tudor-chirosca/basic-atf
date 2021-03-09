package com.vocalink.crossproduct.infrastructure.bps.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BPSSingleFileRequest {

  @JsonInclude(Include.NON_EMPTY)
  private final String instructionId;
  private final String fileName;
}
