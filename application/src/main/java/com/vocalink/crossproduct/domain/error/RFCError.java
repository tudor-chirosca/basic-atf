package com.vocalink.crossproduct.domain.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RFCError {
  private final String source;
  private final String reason;
  private final String message;
  private final boolean recoverable;
}
