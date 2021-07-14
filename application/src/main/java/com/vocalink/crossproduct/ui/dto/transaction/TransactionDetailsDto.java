package com.vocalink.crossproduct.ui.dto.transaction;

import com.vocalink.crossproduct.domain.transaction.OutputType;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionDetailsDto {

  private final String instructionId;
  private final BigDecimal amount;
  private final String currency;
  private final String fileName;
  private final String batchId;
  private final ZonedDateTime settlementDate;
  private final String settlementCycleId;
  private final ZonedDateTime createdAt;
  private final String status;
  private final String reasonCode;
  private final String messageType;
  private final TransactionSenderDetailsDto sender;
  private final TransactionReceiverDetailsDto receiver;
  private final OutputType outputType;
}
