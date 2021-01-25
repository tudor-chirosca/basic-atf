package com.vocalink.crossproduct.infrastructure.bps.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSAccount {

  private final String partyCode;
  private final Integer accountNo;
  private final String iban;

  @JsonCreator
  public BPSAccount(
      @JsonProperty(value = "partyCode", required = true) String partyCode,
      @JsonProperty(value = "accountNo", required = true) Integer accountNo,
      @JsonProperty(value = "iban", required = true) String iban) {
    this.partyCode = partyCode;
    this.accountNo = accountNo;
    this.iban = iban;
  }
}
