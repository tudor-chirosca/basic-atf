package com.vocalink.crossproduct.domain.participant;

import com.vocalink.crossproduct.domain.Amount;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DebitCapThreshold {

  private final String debitCapLimitId;
  private final Amount limitThresholdAmounts;
  private final Double warningThresholdPercentage;
}
