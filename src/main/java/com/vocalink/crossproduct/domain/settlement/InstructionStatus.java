package com.vocalink.crossproduct.domain.settlement;

public enum  InstructionStatus {

  REJECTED("rejected"),
  ACCEPTED("accepted"),
  NO_RESPONSE("no-response");

  private final String description;

  InstructionStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
