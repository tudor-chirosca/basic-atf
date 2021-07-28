package com.vocalink.crossproduct.domain.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class OutputFlow {

  private final String messageType;
  private final Integer txnVolume;
  private final Integer txnTimeLimit;
}
