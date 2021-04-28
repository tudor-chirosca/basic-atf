package com.vocalink.crossproduct.ui.dto.position;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TotalPositionDto {

  private final ParticipantDto participant;
  private final ParticipantPositionDto previousPosition;
  private final ParticipantPositionDto currentPosition;
  @JsonInclude(Include.NON_EMPTY)
  private final IntraDayPositionGrossDto intraDayPositionGross;
}
