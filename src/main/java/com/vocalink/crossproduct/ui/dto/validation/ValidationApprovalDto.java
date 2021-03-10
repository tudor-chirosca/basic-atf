package com.vocalink.crossproduct.ui.dto.validation;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidationApprovalDto {

  private final boolean canExecute;
  private final ZonedDateTime timestamp;
}
