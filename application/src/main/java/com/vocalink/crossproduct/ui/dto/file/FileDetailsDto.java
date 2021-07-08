package com.vocalink.crossproduct.ui.dto.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.domain.reference.MessageReferenceDirection;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class FileDetailsDto {

  private final String fileName;
  private final int nrOfBatches;
  private final long fileSize;
  private final String settlementCycleId;
  private final ZonedDateTime createdAt;
  private final String status;
  @JsonInclude(Include.NON_EMPTY)
  private final String reasonCode;
  private final String messageType;
  private final MessageReferenceDirection messageDirection;
  private final EnquirySenderDetailsDto sender;
  private final EnquirySenderDetailsDto receiver;

}
