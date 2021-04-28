package com.vocalink.crossproduct.ui.dto.batch;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BatchDto {

  private final String id;
  private final ZonedDateTime createdAt;
  private final String senderBic;
  private final String messageType;
  private final int nrOfTransactions;
  private final String status;
}
