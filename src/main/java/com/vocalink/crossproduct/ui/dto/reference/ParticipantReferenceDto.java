package com.vocalink.crossproduct.ui.dto.reference;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.shared.participant.ParticipantType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class ParticipantReferenceDto {

  private final String participantIdentifier;
  private final String name;
  private final ParticipantType participantType;
  private final String schemeCode;

  @Setter
  @JsonInclude(Include.NON_EMPTY)
  private String connectingParticipantId;

  public String getParticipantType() {
    return participantType.getDescription();
  }
}
