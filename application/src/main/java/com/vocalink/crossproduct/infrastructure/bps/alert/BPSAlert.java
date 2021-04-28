package com.vocalink.crossproduct.infrastructure.bps.alert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.participant.BPSParticipant;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSAlert {

  private final Integer alertId;
  private final String priority;
  private final ZonedDateTime dateRaised;
  private final String type;
  private final List<BPSParticipant> entities;

  @JsonCreator
  public BPSAlert(
      @JsonProperty(value = "alertId") Integer alertId,
      @JsonProperty(value = "priority") String priority,
      @JsonProperty(value = "dateRaised") ZonedDateTime dateRaised,
      @JsonProperty(value = "type") String type,
      @JsonProperty(value = "entities") List<BPSParticipant> entities) {
    this.alertId = alertId;
    this.priority = priority;
    this.dateRaised = dateRaised;
    this.type = type;
    this.entities = entities;
  }
}
