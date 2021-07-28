package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSParticipantConfiguration {

  private final String schemeParticipantIdentifier;
  private final String participantName;
  private final String participantBic;
  private final String partyExternalIdentifier;
  private final String participantType;
  private final String connectingParty;
  private final String suspensionLevel;
  private final ZonedDateTime suspendedTime;
  private final String participantConnectionId;
  private final String settlementAccount;
  private final String tpspId;
  private final String tpspName;
  private final String networkName;
  private final String outputChannel;
  private final String status;
  private final List<BPSDebitCapThreshold> debitCapThreshold;
  private final List<BPSOutputFlow> outputFlow;
  private final ZonedDateTime updatedAt;
  private final String updatedBy;

  public BPSParticipantConfiguration(
      @JsonProperty(value = "schemeParticipantIdentifier", required = true) String schemeParticipantIdentifier,
      @JsonProperty(value = "participantName", required = true) String participantName,
      @JsonProperty(value = "participantBic", required = true) String participantBic,
      @JsonProperty(value = "partyExternalIdentifier", required = true) String partyExternalIdentifier,
      @JsonProperty(value = "participantType", required = true) String participantType,
      @JsonProperty(value = "suspensionLevel") String suspensionLevel,
      @JsonProperty(value = "suspendedTime") ZonedDateTime suspendedTime,
      @JsonProperty(value = "connectingParty") String connectingParty,
      @JsonProperty(value = "participantConnectionId") String participantConnectionId,
      @JsonProperty(value = "settlementAccount", required = true) String settlementAccount,
      @JsonProperty(value = "tpspId") String tpspId,
      @JsonProperty(value = "tpspName") String tpspName,
      @JsonProperty(value = "networkName", required = true) String networkName,
      @JsonProperty(value = "outputChannel") String outputChannel,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "debitCapThreshold") List<BPSDebitCapThreshold> debitCapThreshold,
      @JsonProperty(value = "outputFlow") List<BPSOutputFlow> outputFlow,
      @JsonProperty(value = "updatedAt", required = true) ZonedDateTime updatedAt,
      @JsonProperty(value = "updatedBy") String updatedBy) {
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
    this.participantName = participantName;
    this.participantBic = participantBic;
    this.partyExternalIdentifier = partyExternalIdentifier;
    this.participantType = participantType;
    this.suspensionLevel = suspensionLevel;
    this.connectingParty = connectingParty;
    this.participantConnectionId = participantConnectionId;
    this.settlementAccount = settlementAccount;
    this.tpspId = tpspId;
    this.tpspName = tpspName;
    this.networkName = networkName;
    this.outputChannel = outputChannel;
    this.status = status;
    this.debitCapThreshold = debitCapThreshold;
    this.outputFlow = outputFlow;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
    this.suspendedTime = suspendedTime;
  }
}
