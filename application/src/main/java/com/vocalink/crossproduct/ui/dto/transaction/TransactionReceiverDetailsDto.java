package com.vocalink.crossproduct.ui.dto.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionReceiverDetailsDto {

  private final String entityName;
  private final String entityBic;
  @JsonInclude(Include.NON_EMPTY)
  private final String iban;
  @JsonInclude(Include.NON_EMPTY)
  private final String fullName;
  private final String creditorName;
  private final String creditorBic;
}
