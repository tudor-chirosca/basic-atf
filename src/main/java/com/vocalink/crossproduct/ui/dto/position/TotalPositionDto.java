package com.vocalink.crossproduct.ui.dto.position;

import com.vocalink.crossproduct.domain.participant.Participant;
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
  private Participant participant;
  private ParticipantPositionDto previousPosition;
  private ParticipantPositionDto currentPosition;
  private IntraDayPositionGrossDto intraDayPositionGross;
}
