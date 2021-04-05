package com.vocalink.crossproduct.domain.io;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IODashboard {

  private final String fileRejected;
  private final String batchesRejected;
  private final String transactionsRejected;
  private final List<ParticipantIOData> summary;
}
