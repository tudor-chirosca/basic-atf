package com.vocalink.crossproduct.infrastructure.bps.reference;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class StatusWrapper {

  private final String status;

  public StatusWrapper(@JsonProperty("status") String status) {
    this.status = status;
  }
}
