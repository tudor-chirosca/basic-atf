package com.vocalink.crossproduct.domain.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Account {

  private final String partyCode;
  private final String accountNo;
  private final String iban;
}
