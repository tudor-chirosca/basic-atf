package com.vocalink.crossproduct.ui.dto.position;

import com.vocalink.crossproduct.domain.participant.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TotalPositionDto {
  private Participant participant;
  private ParticipantPositionDto previousPosition;
  private ParticipantPositionDto currentPosition;
}
