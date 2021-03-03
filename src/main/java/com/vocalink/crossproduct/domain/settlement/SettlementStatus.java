package com.vocalink.crossproduct.domain.settlement;

public enum SettlementStatus {
  CLOSED("closed"),
  SETTLING("settling"),
  COMPLETED("completed"),
  PARTIAL("partial"),
  NO_RESPONSE("no-response");

  private final String description;

  SettlementStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}
