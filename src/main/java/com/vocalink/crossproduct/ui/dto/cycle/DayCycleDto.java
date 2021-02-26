package com.vocalink.crossproduct.ui.dto.cycle;

import com.vocalink.crossproduct.domain.cycle.CycleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DayCycleDto {

  private final String id;
  private final String sessionCode;
  private final CycleStatus status;
}
