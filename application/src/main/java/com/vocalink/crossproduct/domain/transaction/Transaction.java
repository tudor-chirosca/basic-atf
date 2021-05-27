package com.vocalink.crossproduct.domain.transaction;

import com.vocalink.crossproduct.domain.Amount;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Transaction {

  private final String instructionId;
  private final ZonedDateTime createdAt;
  private final String messageType;
  private final Amount amount;
  private final String status;
  private final String fileName;
  private final String batchId;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final String reasonCode;
  private final String senderBank;
  private final String senderBic;
  private final String senderIBAN;
  private final String senderFullName;
  private final String receiverBank;
  private final String receiverBic;
  private final String receiverIBAN;
  private final String receiverFullName;
}
