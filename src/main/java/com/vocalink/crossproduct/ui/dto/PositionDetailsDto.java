package com.vocalink.crossproduct.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PositionDetailsDto {
  private ParticipantPositionDto previousPosition;
  private ParticipantPositionDto currentPosition;
}