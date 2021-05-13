package com.vocalink.crossproduct.domain.position;

import com.vocalink.crossproduct.domain.Amount;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantPosition {

  private final LocalDate settlementDate;
  private final String participantId;
  private final String cycleId;
  private final String currency;
  private final Payment paymentSent;
  private final Payment paymentReceived;
  private final Payment returnSent;
  private final Payment returnReceived;
  private final Amount netPositionAmount;
}
