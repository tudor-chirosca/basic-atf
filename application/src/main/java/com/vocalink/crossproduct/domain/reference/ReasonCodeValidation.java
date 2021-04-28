package com.vocalink.crossproduct.domain.reference;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReasonCodeValidation {

  private final EnquiryType validationLevel;
  private final List<ReasonCode> reasonCodes;

  @Getter
  @AllArgsConstructor
  public static class ReasonCode {

    private final String reasonCode;
    private final String description;
    private final Boolean active;
  }
}
