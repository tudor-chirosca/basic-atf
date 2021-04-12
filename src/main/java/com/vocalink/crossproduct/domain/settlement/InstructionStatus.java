package com.vocalink.crossproduct.domain.settlement;

import lombok.Getter;

@Getter
public enum InstructionStatus {

  CREATED("Created"),
  REJECTED("Rejected"),
  SENT("Sent"),
  SETTLED("Settled"),
  SUBMITTED("Submitted");

  private final String description;

  InstructionStatus(String description) {
    this.description = description;
  }
}
