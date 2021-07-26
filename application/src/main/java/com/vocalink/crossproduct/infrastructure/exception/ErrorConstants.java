package com.vocalink.crossproduct.infrastructure.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {

  public static final String ERROR_SOURCE_ISS = "ISS";
  public static final String ERROR_SOURCE_BPS = "BPS";

  public static final String ERROR_REASON_INVALID_INPUT = "INVALID_INPUT";
  public static final String ERROR_REASON_INTERNAL_ERROR = "INTERNAL_SERVER_ERROR";
  public static final String ERROR_REASON_INTEGRATION_ERROR = "INTEGRATION_SERVER_ERROR";
}
