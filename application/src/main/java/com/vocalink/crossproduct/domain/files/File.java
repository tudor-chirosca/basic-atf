package com.vocalink.crossproduct.domain.files;

import com.vocalink.crossproduct.domain.reference.MessageReferenceDirection;
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
  private final String from;
  private final String to;
  private final String messageType;
  private final MessageReferenceDirection messageDirection;
  private final int noOfBatches;
  private final String status;
  private final String reasonCode;
  private final String settlementCycle;
}
