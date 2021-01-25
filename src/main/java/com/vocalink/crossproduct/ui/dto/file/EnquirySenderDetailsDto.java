package com.vocalink.crossproduct.ui.dto.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class EnquirySenderDetailsDto {

  private final String entityName;
  private final String entityBic;
  @JsonInclude(Include.NON_EMPTY)
  private final String iban;
  @JsonInclude(Include.NON_EMPTY)
  private final String fullName;
}
