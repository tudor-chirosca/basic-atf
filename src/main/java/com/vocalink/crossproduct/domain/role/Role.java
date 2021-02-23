package com.vocalink.crossproduct.domain.role;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Role {
  private final String id;
  private final Function function;

  public enum Function {
    SCHEME_READ_ONLY,
    SCHEME_MANAGEMENT,
    MANAGEMENT,
    CLEARING,
    SETTLEMENT,
    READ_ONLY
  }
}
