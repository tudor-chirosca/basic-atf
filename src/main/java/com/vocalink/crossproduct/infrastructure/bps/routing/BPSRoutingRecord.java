package com.vocalink.crossproduct.infrastructure.bps.routing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSRoutingRecord {

  private final String schemeParticipantIdentifier;
  private final String reachableBic;
  private final ZonedDateTime validFrom;
  private final ZonedDateTime validTo;
  private final String currency;

  @JsonCreator
  public BPSRoutingRecord(
      @JsonProperty(value = "schemeParticipantIdentifier", required = true) String schemeParticipantIdentifier,
      @JsonProperty(value = "reachableBic", required = true) String reachableBic,
      @JsonProperty(value = "validFrom", required = true) ZonedDateTime validFrom,
      @JsonProperty(value = "validTo") ZonedDateTime validTo,
      @JsonProperty(value = "currency") String currency) {
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
    this.reachableBic = reachableBic;
    this.validFrom = validFrom;
    this.validTo = validTo;
    this.currency = currency;
  }
}
