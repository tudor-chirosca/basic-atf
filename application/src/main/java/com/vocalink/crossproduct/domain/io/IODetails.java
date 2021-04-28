package com.vocalink.crossproduct.domain.io;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class IODetails {

  private final IODataDetails files;
  private final List<IOBatchesMessageTypes> batches;
  private final List<IOTransactionsMessageTypes> transactions;
}
