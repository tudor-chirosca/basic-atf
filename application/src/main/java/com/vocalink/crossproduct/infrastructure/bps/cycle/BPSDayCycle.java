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
      @JsonProperty(value = "cycleCode", required = true) String cycleCode,
      @JsonProperty(value = "sessionCode", required = true) String sessionCode,
      @JsonProperty(value = "sessionInstanceId", required = true) String sessionInstanceId,
      @JsonProperty(value = "status", required = true) CycleStatus status,
      @JsonProperty(value = "createdDate", required = true) ZonedDateTime createdDate,
      @JsonProperty(value = "updatedDate") ZonedDateTime updatedDate) {
    this.cycleCode = cycleCode;
    this.sessionCode = sessionCode;
    this.sessionInstanceId = sessionInstanceId;
    this.status = status;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
  }
}
