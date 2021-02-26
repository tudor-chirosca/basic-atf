package com.vocalink.crossproduct.infrastructure.bps.cycle;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSDayCycleRequest {

  private final String schemeCode;
  private final LocalDate settlementDate;
}
