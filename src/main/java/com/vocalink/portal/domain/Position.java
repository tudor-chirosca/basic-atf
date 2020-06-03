package com.vocalink.portal.domain;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Position {

  private String participantId;
  private BigInteger credit;
  private BigInteger debit;
  private BigInteger netPosition;
}
