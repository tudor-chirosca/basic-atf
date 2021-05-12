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

  private final LocalDate dateFrom;
  private final String filesRejected;
  private final String batchesRejected;
  private final String transactionsRejected;
  private final List<ParticipantIODataDto> rows;
}
