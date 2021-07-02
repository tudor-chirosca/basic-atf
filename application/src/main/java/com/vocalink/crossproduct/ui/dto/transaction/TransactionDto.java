package com.vocalink.crossproduct.ui.dto.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionDto {

  private final String instructionId;
  private final ZonedDateTime createdAt;
  private final String senderBic;
  private final String receiverBic;
  private final String messageType;
  private final BigDecimal amount;
  private final String status;
}
