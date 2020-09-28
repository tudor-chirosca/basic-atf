package com.vocalink.crossproduct.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PositionDetails {

  private String sessionCode;
  private ParticipantPosition customerCreditTransfer;
  private ParticipantPosition paymentReturn;
}
