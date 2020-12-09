package com.vocalink.crossproduct.domain.position;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantPosition {

  private final String participantId;
  public final BigDecimal credit;
  private final BigDecimal debit;
  private final BigDecimal netPosition;
}
