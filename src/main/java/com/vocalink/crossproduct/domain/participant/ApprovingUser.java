package com.vocalink.crossproduct.domain.participant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApprovingUser {

  private final String schemeParticipantIdentifier;
  private final String firstName;
  private final String userId;
  private final String lastName;
}
