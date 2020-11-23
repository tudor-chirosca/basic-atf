package com.vocalink.crossproduct.domain.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantReference {

  private final String participantIdentifier;
  private final String name;
}
