package com.vocalink.crossproduct.domain.batch;

import java.time.ZonedDateTime;

import com.vocalink.crossproduct.domain.reference.MessageReferenceDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Batch {

  private final String batchId;
  private final int nrOfTransactions;
  private final String fileName;
  private final ZonedDateTime settlementDate;
  private final String settlementCycleId;
  private final ZonedDateTime createdAt;
  private final String status;
  private final String reasonCode;
  private final String messageType;
  private final MessageReferenceDirection messageDirection;
  private final String senderBank;
  private final String senderBic;
  private final String receiverBank;
  private final String receiverBic;
}
