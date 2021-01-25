package com.vocalink.crossproduct.domain.approval;

import lombok.Getter;

@Getter
public enum ApprovalStatus {
  PENDING("pending"),
  APPROVED("approved"),
  REJECTED("rejected");

  private final String description;

  ApprovalStatus(String description) {
    this.description = description;
  }
}
