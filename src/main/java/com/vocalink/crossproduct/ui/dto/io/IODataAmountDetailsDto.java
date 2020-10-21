package com.vocalink.crossproduct.ui.dto.io;

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
public class IODataAmountDetailsDto {

  private Integer submitted;
  private Integer accepted;
  private Integer output;
  private Double rejected;
  private Integer amountAccepted;
  private Integer amountOutput;
}
