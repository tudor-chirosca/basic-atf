package com.vocalink.crossproduct.ui.dto.position;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantPositionDto {

  private final BigDecimal credit;
  private final BigDecimal debit;
  private final BigDecimal netPosition;
}
