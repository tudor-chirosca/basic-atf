package com.vocalink.crossproduct.domain.reference;

import com.vocalink.crossproduct.shared.participant.ParticipantType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantReference {

  private final String participantIdentifier;
  private final String name;
  private final ParticipantType participantType;
  private final String connectingParticipantId;
  private final String schemeCode;
}
