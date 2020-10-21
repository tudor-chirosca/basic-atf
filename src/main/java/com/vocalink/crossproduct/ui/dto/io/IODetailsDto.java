package com.vocalink.crossproduct.ui.dto.io;

import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IODetailsDto {

  private ParticipantDto participant;
  private LocalDate dateFrom;
  private IODataDetailsDto files;
  private List<IOBatchesMessageTypesDto> batches;
  private List<IOTransactionsMessageTypesDto> transactions;
}
