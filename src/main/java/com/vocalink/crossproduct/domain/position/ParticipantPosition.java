package com.vocalink.crossproduct.domain.position;

import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto;
import java.math.BigDecimal;
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
  public BigDecimal credit;
  private BigDecimal debit;
  private BigDecimal netPosition;

  public ParticipantPositionDto toDto(){
    return ParticipantPositionDto.builder()
        .credit(credit)
        .debit(debit)
        .netPosition(netPosition)
        .build();
  }
}
