package com.vocalink.crossproduct.ui.dto.audit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParticipantDetailsDto {

  private final String bic;
  private final String participantName;
}
