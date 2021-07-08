package com.vocalink.crossproduct.ui.dto.batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.domain.reference.MessageReferenceDirection;
import com.vocalink.crossproduct.ui.dto.file.EnquiryUserDetailsDto;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BatchDetailsDto {

  private final String batchId;
  private final String fileName;
  private final int nrOfTransactions;
  private final ZonedDateTime settlementDate;
  private final String settlementCycleId;
  private final ZonedDateTime createdAt;
  private final String status;
  @JsonInclude(Include.NON_EMPTY)
  private final String reasonCode;
  private final String messageType;
  private final MessageReferenceDirection messageDirection;
  private final EnquiryUserDetailsDto sender;
  private final EnquiryUserDetailsDto receiver;
}
