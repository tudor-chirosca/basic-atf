package com.vocalink.crossproduct.infrastructure.bps.alert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSAlertReferenceData {

  private final List<BPSAlertPriority> priorities;
  private final List<String> alertTypes;

  @JsonCreator
  public BPSAlertReferenceData(
      @JsonProperty(value = "priorities") List<BPSAlertPriority> priorities,
      @JsonProperty(value = "alertTypes") List<String> alertTypes) {
    this.priorities = priorities;
    this.alertTypes = alertTypes;
  }
}
