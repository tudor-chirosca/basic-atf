package com.vocalink.crossproduct.domain.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IODataDetails {

  private final Integer submitted;
  private final Integer accepted;
  private final Integer output;
  private final Double rejected;
}
