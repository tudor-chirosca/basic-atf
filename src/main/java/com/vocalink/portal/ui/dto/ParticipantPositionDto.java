package com.vocalink.portal.ui.dto;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParticipantPositionDto {
  private BigInteger credit;
  private BigInteger debit;
  private BigInteger netPosition;
}
