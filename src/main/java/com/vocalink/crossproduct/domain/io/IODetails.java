package com.vocalink.crossproduct.domain.io;

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
public class IODetails {

  private String schemeParticipantIdentifier;
  private IODataDetails files;
  private List<IOBatchesMessageTypes> batches;
  private List<IOTransactionsMessageTypes> transactions;
}
