package com.vocalink.crossproduct.ui.dto.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IODataDetailsDto {

  private final Integer submitted;
  private final Integer accepted;
  private final Integer output;
  private final Double rejected;
}
