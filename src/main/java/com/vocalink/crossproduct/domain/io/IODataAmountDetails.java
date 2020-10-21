package com.vocalink.crossproduct.domain.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IODataAmountDetails {

  private Integer submitted;
  private Integer accepted;
  private Integer output;
  private Double rejected;
  private Integer amountAccepted;
  private Integer amountOutput;
}
