package com.vocalink.crossproduct.domain.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OutputFlowReference {

  private final String messageType;
  private final Boolean editable;
  private final Integer outputVolumeMin;
  private final Integer outputVolumeMax;
  private final Integer outputTimeMin;
  private final Integer outputTimeMax;
}
