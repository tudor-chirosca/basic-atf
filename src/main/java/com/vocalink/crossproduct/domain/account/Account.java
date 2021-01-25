package com.vocalink.crossproduct.domain.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Account {

  private final String partyCode;
  private final Integer accountNo;
  private final String iban;
}
