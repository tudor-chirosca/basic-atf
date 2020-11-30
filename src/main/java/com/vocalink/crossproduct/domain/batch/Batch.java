package com.vocalink.crossproduct.domain.batch;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Batch {

  private final String id;
  private final LocalDateTime createdAt;
  private final String senderBic;
  private final String messageType;
  private final int nrOfTransactions;
  private final String status;
}
