package com.vocalink.crossproduct.ui.dto.file;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FileDto {

  private final String name;
  private final LocalDateTime createdAt;
  private final String senderBic;
  private final String messageType;
  private final Integer nrOfBatches;
  private final String status;
}
