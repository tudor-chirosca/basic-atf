package com.vocalink.crossproduct.ui.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IODashboardDto {
  private LocalDate dateFrom;
  private String filesRejected;
  private String batchesRejected;
  private String transactionsRejected;
  private List<ParticipantIODataDto> rows;

}
