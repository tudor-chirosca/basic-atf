package com.vocalink.crossproduct.ui.dto.batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.ui.dto.file.EnquirySenderDetailsDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@Builder
@AllArgsConstructor
public class BatchDetailsDto {

  private final String batchId;
  private final String fileName;
  private final int nrOfTransactions;
  private final long fileSize;
  private final LocalDate settlementDate;
  private final String settlementCycleId;
  private final LocalDateTime createdAt;
  private final String status;
  @JsonInclude(Include.NON_EMPTY)
  private final String reasonCode;
  private final String messageType;
  private final EnquirySenderDetailsDto sender;
}
