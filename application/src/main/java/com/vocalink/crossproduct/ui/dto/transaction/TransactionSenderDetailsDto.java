package com.vocalink.crossproduct.ui.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionSenderDetailsDto {

  private final String entityName;
  private final String entityBic;
  private final String iban;
  private final String fullName;
  private final String debtorName;
  private final String debtorBic;
}
