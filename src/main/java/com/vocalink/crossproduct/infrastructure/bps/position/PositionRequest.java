package com.vocalink.crossproduct.infrastructure.bps.position;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PositionRequest {
  private String schemeCode;
  private List<String> sessionCodes;
  private String schemeParticipantIdentifier;
}
