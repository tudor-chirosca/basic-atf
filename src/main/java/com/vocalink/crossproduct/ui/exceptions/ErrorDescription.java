package com.vocalink.crossproduct.ui.exceptions;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDescription {

  private final String timestamp;
  private final String errorCode;
  private final int httpStatus;
  private final String message;
  private final String additionalInfo;
  private final String path;
}
