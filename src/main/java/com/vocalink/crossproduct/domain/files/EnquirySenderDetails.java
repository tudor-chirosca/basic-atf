package com.vocalink.crossproduct.domain.files;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EnquirySenderDetails {

  private final String entityName;
  private final String entityBic;
  private final String iban;
  private final String fullName;
}
