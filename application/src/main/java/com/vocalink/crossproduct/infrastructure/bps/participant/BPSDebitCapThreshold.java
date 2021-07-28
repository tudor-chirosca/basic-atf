package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import lombok.Getter;

@Getter
public class BPSDebitCapThreshold {

  private final String debitCapLimitId;
  private final BPSAmount limitThresholdAmounts;
  private final Double warningThresholdPercentage;

  public BPSDebitCapThreshold(
      @JsonProperty(value = "debitCapLimitId") String debitCapLimitId,
      @JsonProperty(value = "limitThresholdAmounts") BPSAmount limitThresholdAmounts,
      @JsonProperty(value = "warningThresholdPercentage") Double warningThresholdPercentage) {
    this.debitCapLimitId = debitCapLimitId;
    this.limitThresholdAmounts = limitThresholdAmounts;
    this.warningThresholdPercentage = warningThresholdPercentage;
  }
}
