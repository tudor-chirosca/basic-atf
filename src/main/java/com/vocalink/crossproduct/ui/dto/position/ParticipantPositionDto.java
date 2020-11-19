package com.vocalink.crossproduct.ui.dto.position;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParticipantPositionDto {

  private BigDecimal credit;
  private BigDecimal debit;
  private BigDecimal netPosition;
}
