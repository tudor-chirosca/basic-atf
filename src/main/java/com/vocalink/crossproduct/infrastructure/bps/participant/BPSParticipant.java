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
  private final String organizationId;
  private final String tpspName;
  private final String tpspId;

  @JsonCreator
  public BPSParticipant(
      @JsonProperty(value = "schemeCode", required = true) String schemeCode,
      @JsonProperty(value = "schemeParticipantIdentifier", required = true) String schemeParticipantIdentifier,
      @JsonProperty(value = "countryCode") String countryCode,
      @JsonProperty(value = "partyCode", required = true) String partyCode,
      @JsonProperty(value = "participantType", required = true) String participantType,
      @JsonProperty(value = "connectingParty") String connectingParty,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "effectiveFromDate", required = true) ZonedDateTime effectiveFromDate,
      @JsonProperty(value = "effectiveTillDate") ZonedDateTime effectiveTillDate,
      @JsonProperty(value = "participantName", required = true) String participantName,
      @JsonProperty(value = "rcvngParticipantConnectionId", required = true) String rcvngParticipantConnectionId,
      @JsonProperty(value = "participantConnectionId", required = true) String participantConnectionId,
      @JsonProperty(value = "organizationId") String organizationId,
      @JsonProperty(value = "tpspName") String tpspName,
      @JsonProperty(value = "tpspId") String tpspId) {
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
    this.organizationId = organizationId;
    this.tpspName = tpspName;
    this.tpspId = tpspId;
  }
}
