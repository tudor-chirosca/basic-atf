package com.vocalink.crossproduct.infrastructure.bps.participant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSParticipantConfiguration {

  private final String schemeParticipantIdentifier;
  private final Integer txnVolume;
  private final Integer outputFileTimeLimit;
  private final String networkName;
  private final String gatewayName;
  private final String requestorDN;
  private final String responderDN;
  private final String preSettlementAckType;
  private final String preSettlementActGenerationLevel;
  private final String postSettlementAckType;
  private final String postSettlementAckGenerationLevel;
  private final BigDecimal debitCapLimit;
  private final List<Double> debitCapLimitThresholds;
  private final ZonedDateTime updatedAt;
  private final BPSApprovingUser updatedBy;

  @JsonCreator
  public BPSParticipantConfiguration(
      @JsonProperty(value = "schemeParticipantIdentifier", required = true) String schemeParticipantIdentifier,
      @JsonProperty(value = "txnVolume", required = true) Integer txnVolume,
      @JsonProperty(value = "outputFileTimeLimit") Integer outputFileTimeLimit,
      @JsonProperty(value = "networkName") String networkName,
      @JsonProperty(value = "gatewayName") String gatewayName,
      @JsonProperty(value = "requestorDN") String requestorDN,
      @JsonProperty(value = "responderDN") String responderDN,
      @JsonProperty(value = "preSettlementAckType") String preSettlementAckType,
      @JsonProperty(value = "preSettlementActGenerationLevel") String preSettlementActGenerationLevel,
      @JsonProperty(value = "postSettlementAckType") String postSettlementAckType,
      @JsonProperty(value = "postSettlementAckGenerationLevel") String postSettlementAckGenerationLevel,
      @JsonProperty(value = "debitCapLimit") BigDecimal debitCapLimit,
      @JsonProperty(value = "debitCapLimitThresholds") List<Double> debitCapLimitThresholds,
      @JsonProperty(value = "updatedAt") ZonedDateTime updatedAt,
      @JsonProperty(value = "updatedBy") BPSApprovingUser updatedBy) {
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
    this.txnVolume = txnVolume;
    this.outputFileTimeLimit = outputFileTimeLimit;
    this.networkName = networkName;
    this.gatewayName = gatewayName;
    this.requestorDN = requestorDN;
    this.responderDN = responderDN;
    this.preSettlementAckType = preSettlementAckType;
    this.preSettlementActGenerationLevel = preSettlementActGenerationLevel;
    this.postSettlementAckType = postSettlementAckType;
    this.postSettlementAckGenerationLevel = postSettlementAckGenerationLevel;
    this.debitCapLimit = debitCapLimit;
    this.debitCapLimitThresholds = debitCapLimitThresholds;
    this.updatedAt = updatedAt;
    this.updatedBy = updatedBy;
  }
}
