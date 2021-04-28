package com.vocalink.crossproduct.domain.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IOData {

  private final Integer submitted;
  private final String rejected;
  private final Integer output;
}
