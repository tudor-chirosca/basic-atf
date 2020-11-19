package com.vocalink.crossproduct.ui.dto.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FileDetailsDto {

  private final String fileName;
  private final int nrOfBatches;
  private final long fileSize;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final LocalDateTime createdAt;
  private final String status;
  @JsonInclude(Include.NON_EMPTY)
  private final String reasonCode;
  private final String messageType;
  private final FileSenderDetailsDto sender;
}
