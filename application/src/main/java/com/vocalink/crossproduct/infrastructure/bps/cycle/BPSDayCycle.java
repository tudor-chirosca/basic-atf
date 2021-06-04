package com.vocalink.crossproduct.infrastructure.bps.cycle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.domain.cycle.CycleStatus;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BPSDayCycle {

  private final String cycleCode;
  private final String sessionCode;
  @Setter
  private String sessionInstanceId;
  private final CycleStatus status;
  @Setter
  private ZonedDateTime createdDate;
  @Setter
  private ZonedDateTime updatedDate;

  public BPSDayCycle(
      @JsonProperty(value = "cycleCode") String cycleCode,
      @JsonProperty(value = "sessionCode") String sessionCode,
      @JsonProperty(value = "sessionInstanceId") String sessionInstanceId,
      @JsonProperty(value = "status") CycleStatus status,
      @JsonProperty(value = "createdDate") ZonedDateTime createdDate,
      @JsonProperty(value = "updatedDate") ZonedDateTime updatedDate) {
    this.cycleCode = cycleCode;
    this.sessionCode = sessionCode;
    this.sessionInstanceId = sessionInstanceId;
    this.status = status;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }
}
