package com.vocalink.crossproduct.ui.dto.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IODataDto {

  private final Integer submitted;
  private final Double rejected;
  @JsonInclude(Include.NON_EMPTY)
  private final Integer output;
}
