package com.vocalink.crossproduct.domain.files;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class File {

  private final String instructionId;
  private final String fileName;
  private final long fileSize;
  private final ZonedDateTime createdDate;
  private final String originator;
  private final String messageType;
  private final int nrOfBatches;
  private final String status;
  private final String reasonCode;
  private final String settlementCycle;
  private final ZonedDateTime settlementDate;
  private final String schemeParticipantIdentifier;
}
