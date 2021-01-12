package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSParticipant {

  private final String schemeCode;
  private final String schemeParticipantIdentifier;
  private final String countryCode;
  private final String partyCode;
  private final String participantType;
  private final String connectingParty;
  private final String status;
  private final ZonedDateTime effectiveFromDate;
  private final ZonedDateTime effectiveTillDate;
  private final String participantName;
  private final String rcvngParticipantConnectionId;
  private final String participantConnectionId;

  @JsonCreator
  public BPSParticipant(
      @JsonProperty(value = "schemeCode", required = true) String schemeCode,
      @JsonProperty(value = "schemeParticipantIdentifier", required = true) String schemeParticipantIdentifier,
      @JsonProperty(value = "countryCode", required = true) String countryCode,
      @JsonProperty(value = "partyCode", required = true) String partyCode,
      @JsonProperty(value = "participantType", required = true) String participantType,
      @JsonProperty(value = "connectingParty", required = true) String connectingParty,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "effectiveFromDate", required = true) ZonedDateTime effectiveFromDate,
      @JsonProperty(value = "effectiveTillDate", required = true) ZonedDateTime effectiveTillDate,
      @JsonProperty(value = "participantName", required = true) String participantName,
      @JsonProperty(value = "rcvngParticipantConnectionId", required = true) String rcvngParticipantConnectionId,
      @JsonProperty(value = "participantConnectionId", required = true) String participantConnectionId) {
    this.schemeCode = schemeCode;
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
    this.countryCode = countryCode;
    this.partyCode = partyCode;
    this.participantType = participantType;
    this.connectingParty = connectingParty;
    this.status = status;
    this.effectiveFromDate = effectiveFromDate;
    this.effectiveTillDate = effectiveTillDate;
    this.participantName = participantName;
    this.rcvngParticipantConnectionId = rcvngParticipantConnectionId;
    this.participantConnectionId = participantConnectionId;
  }
}
