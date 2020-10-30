package com.vocalink.crossproduct.ui.exceptions;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDescription {

  private String timestamp;
  private String errorCode;
  private int httpStatus;
  private String message;
  private String additionalInfo;
  private String path;
}
