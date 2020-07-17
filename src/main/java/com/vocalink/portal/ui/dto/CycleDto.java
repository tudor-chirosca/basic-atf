package com.vocalink.portal.ui.dto;

import com.vocalink.portal.domain.CycleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CycleDto {
  private String id;
  private String settlementTime;
  private String cutOffTime;
  private CycleStatus status;
}
