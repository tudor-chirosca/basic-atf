package com.vocalink.crossproduct.domain.participant;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantConfiguration {

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
  private final ApprovingUser updatedBy;
}
