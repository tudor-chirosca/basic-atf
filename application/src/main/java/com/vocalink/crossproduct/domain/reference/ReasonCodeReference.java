package com.vocalink.crossproduct.domain.reference;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class ReasonCodeReference {

  private final List<Validation> validations;

  @Getter
  @RequiredArgsConstructor
  public static class Validation {

    private final String validationLevel;
    private final List<ReasonCode> reasonCodes;
  }

  @Getter
  @RequiredArgsConstructor
  public static class ReasonCode {

    private final String reasonCode;
    private final String description;
    private final Boolean active;
  }
}
