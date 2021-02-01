package com.vocalink.crossproduct.domain.approval;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalUser {

  private final String name;
  private final String id;
  private final String participantName;
}
