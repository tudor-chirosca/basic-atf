package com.vocalink.crossproduct.ui.dto.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PositionDetailsDto {
  private ParticipantPositionDto customerCreditTransfer;
  private ParticipantPositionDto paymentReturn;
}