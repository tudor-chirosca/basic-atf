package com.vocalink.crossproduct.domain.position;

import com.vocalink.crossproduct.domain.Amount;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Payment {

  private final Long count;
  private final Amount amount;
}
