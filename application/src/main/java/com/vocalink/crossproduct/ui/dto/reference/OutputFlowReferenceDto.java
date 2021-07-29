package com.vocalink.crossproduct.ui.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OutputFlowReferenceDto {

  private final String messageType;
  private final Boolean editable;
  private final Integer outputVolumeMin;
  private final Integer outputVolumeMax;
  private final Integer outputTimeMin;
  private final Integer outputTimeMax;
}
