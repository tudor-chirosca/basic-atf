package com.vocalink.crossproduct.ui.dto.position;

import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TotalPositionDto {
  private ParticipantDto participant;
  private ParticipantPositionDto previousPosition;
  private ParticipantPositionDto currentPosition;
  private IntraDayPositionGrossDto intraDayPositionGross;
}
