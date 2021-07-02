package com.vocalink.crossproduct.ui.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionReceiverDetailsDto {

  private final String entityName;
  private final String entityBic;
  private final String iban;
  private final String creditorName;
  private final String creditorBic;
}
