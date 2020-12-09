package com.vocalink.crossproduct.domain.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IOBatchesMessageTypes {

  private final String name;
  private final String code;
  private final IODataDetails data;
}
