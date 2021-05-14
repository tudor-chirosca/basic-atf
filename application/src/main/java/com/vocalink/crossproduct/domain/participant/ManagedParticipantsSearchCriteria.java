package com.vocalink.crossproduct.domain.participant;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ManagedParticipantsSearchCriteria {
  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final String q;
}
