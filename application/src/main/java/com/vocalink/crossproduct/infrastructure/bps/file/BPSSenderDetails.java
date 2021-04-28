package com.vocalink.crossproduct.infrastructure.bps.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSSenderDetails {

  private final String entityName;
  private final String entityBic;
  @JsonInclude(Include.NON_EMPTY)
  private final String iban;
  @JsonInclude(Include.NON_EMPTY)
  private final String fullName;

  @JsonCreator
  public BPSSenderDetails(
      @JsonProperty(value = "entityName", required = true) String entityName,
      @JsonProperty(value = "entityBic", required = true) String entityBic,
      @JsonProperty(value = "iban") String iban,
      @JsonProperty(value = "fullName") String fullName) {
    this.entityName = entityName;
    this.entityBic = entityBic;
    this.iban = iban;
    this.fullName = fullName;
  }
}
