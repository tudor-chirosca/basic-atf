package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSApprovalUser {

  private final String name;
  private final String id;
  private final String participantName;

  @JsonCreator
  public BPSApprovalUser(
      @JsonProperty(value = "name", required = true) String name,
      @JsonProperty(value = "id", required = true) String id,
      @JsonProperty(value = "participantName", required = true) String participantName) {
    this.name = name;
    this.id = id;
    this.participantName = participantName;
  }
}
