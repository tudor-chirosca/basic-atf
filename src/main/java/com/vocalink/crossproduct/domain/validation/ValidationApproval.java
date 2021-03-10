package com.vocalink.crossproduct.domain.validation;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationApproval {

  private final boolean canExecute;
  private final ZonedDateTime timestamp;
}
