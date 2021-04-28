package com.vocalink.crossproduct.domain.transaction;

import com.vocalink.crossproduct.domain.Amount;
import com.vocalink.crossproduct.domain.files.EnquirySenderDetails;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Transaction {

  private final String instructionId;
  private final ZonedDateTime createdAt;
  private final String originator;
  private final String messageType;
  private final Amount amount;
  private final String status;

  private final String fileName;
  private final String batchId;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final String reasonCode;
  private final EnquirySenderDetails sender;
  private final EnquirySenderDetails receiver;
}
