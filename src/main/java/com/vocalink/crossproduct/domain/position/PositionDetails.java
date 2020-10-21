package com.vocalink.crossproduct.domain.position;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionDetails {

  private String sessionCode;
  private ParticipantPosition customerCreditTransfer;
  private ParticipantPosition paymentReturn;
}
