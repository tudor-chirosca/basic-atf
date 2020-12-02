package com.vocalink.crossproduct.domain.batch;

import com.vocalink.crossproduct.domain.files.EnquirySenderDetails;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  private final long fileSize;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final LocalDateTime createdAt;
  private final String status;
  private final String reasonCode;
  private final String messageType;
  private final EnquirySenderDetails sender;

}
