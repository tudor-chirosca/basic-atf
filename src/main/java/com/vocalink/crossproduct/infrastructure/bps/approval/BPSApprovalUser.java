package com.vocalink.crossproduct.infrastructure.bps.approval;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSApprovalUser {

  private final String name;
  private final String id;

  @JsonCreator
  public BPSApprovalUser(
      @JsonProperty(value = "name") String name,
      @JsonProperty(value = "id") String id) {
    this.name = name;
    this.id = id;
  }
}
