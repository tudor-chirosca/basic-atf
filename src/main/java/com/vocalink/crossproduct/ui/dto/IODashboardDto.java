package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.ui.dto.io.ParticipantIODataDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IODashboardDto {

  private LocalDate dateFrom;
  private String filesRejected;
  private String batchesRejected;
  private String transactionsRejected;
  private List<ParticipantIODataDto> rows;
}
