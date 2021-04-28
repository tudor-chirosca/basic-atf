package com.vocalink.crossproduct.domain.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserDetails {

  private final String userId;
  private final String firstName;
  private final String lastName;
  private final String participantId;
}
