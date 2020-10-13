package com.vocalink.crossproduct.domain;

import com.vocalink.crossproduct.ui.dto.ParticipantPositionDto;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantPosition {
  private String participantId;
  public BigInteger credit;
  private BigInteger debit;
  private BigInteger netPosition;

  public ParticipantPositionDto toDto(){
    return ParticipantPositionDto.builder()
        .credit(credit)
        .debit(debit)
        .netPosition(netPosition)
        .build();
  }
}
