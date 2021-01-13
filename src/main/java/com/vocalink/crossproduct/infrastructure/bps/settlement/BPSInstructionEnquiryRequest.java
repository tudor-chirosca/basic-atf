package com.vocalink.crossproduct.infrastructure.bps.settlement;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSInstructionEnquiryRequest {

  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final String cycleId;
  private final String participantId;
}
