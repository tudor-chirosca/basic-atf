package com.vocalink.crossproduct.ui.dto.transaction;

import com.vocalink.crossproduct.ui.dto.file.EnquirySenderDetailsDto;
import java.math.BigDecimal;
import java.time.LocalDate;
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
  private final LocalDate valueDate;
  private final EnquirySenderDetailsDto receiver;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final ZonedDateTime createdAt;
  private final String status;
  private final String reasonCode;
  private final String messageType;
  private final EnquirySenderDetailsDto sender;
}
