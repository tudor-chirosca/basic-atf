package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.domain.CycleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class CycleDto {
  private String id;
  private String settlementTime;
  private String cutOffTime;
  private CycleStatus status;
}
