package com.vocalink.crossproduct.ui.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantReferenceDto {

  private final String participantIdentifier;
  private final String name;
}
