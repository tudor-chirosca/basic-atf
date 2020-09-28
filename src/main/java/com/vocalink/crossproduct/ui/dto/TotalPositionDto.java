package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.domain.Participant;
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
