package com.vocalink.crossproduct.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IOData {
  private int submitted;
  private double rejected;
}
