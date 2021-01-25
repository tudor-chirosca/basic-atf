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
  private final Amount amount;
  private final String fileName;
  private final String batchId;
  private final LocalDate valueDate;
  private final String receiverParticipantIdentifier;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final ZonedDateTime createdAt;
  private final String status;
  private final String reasonCode;
  private final String messageType;
  private final String senderParticipantIdentifier;
}
