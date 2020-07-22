package com.vocalink.portal.domain;

import com.vocalink.portal.ui.dto.ParticipantPositionDto;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParticipantPosition {
  private String participantId;
  private BigInteger credit;
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
