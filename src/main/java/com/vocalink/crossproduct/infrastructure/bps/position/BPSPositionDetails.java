package com.vocalink.crossproduct.infrastructure.bps.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BPSPositionDetails {
  private String schemeCode;
  private String schemeParticipantIdentifier;
  private String sessionCode;
  private String settlementIndicator;
  private BPSPosition customerCreditTransfer;
  private BPSPosition paymentReturn;

}
