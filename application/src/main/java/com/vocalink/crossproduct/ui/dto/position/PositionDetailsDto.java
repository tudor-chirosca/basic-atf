package com.vocalink.crossproduct.ui.dto.position;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PositionDetailsDto {

  private final ParticipantPositionDto customerCreditTransfer;
  private final ParticipantPositionDto paymentReturn;
}
