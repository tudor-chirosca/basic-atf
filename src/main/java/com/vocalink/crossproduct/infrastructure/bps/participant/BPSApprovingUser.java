package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSApprovingUser {

  private final String schemeParticipantIdentifier;
  private final String firstName;
  private final String userId;
  private final String lastName;

  @JsonCreator
  public BPSApprovingUser(
      @JsonProperty(value = "schemeParticipantIdentifier") String schemeParticipantIdentifier,
      @JsonProperty(value = "firstName") String firstName,
      @JsonProperty(value = "userId") String userId,
      @JsonProperty(value = "lastName") String lastName) {
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
    this.firstName = firstName;
    this.userId = userId;
    this.lastName = lastName;
  }
}
