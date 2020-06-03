package com.vocalink.portal.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Cycle {

  private String id;
  private String settlementTime;
  private String cutOffTime;
  private List<Position> positions;
}
