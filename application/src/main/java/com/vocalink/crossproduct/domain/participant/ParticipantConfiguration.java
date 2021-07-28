package com.vocalink.crossproduct.domain.participant;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantConfiguration {

  private final String schemeParticipantIdentifier;
  private final String participantName;
  private final String participantBic;
  private final String partyExternalIdentifier;
  private final ParticipantType participantType;
  private final String suspensionLevel;
  private final ZonedDateTime suspendedTime;
  private final String connectingParty;
  private final String participantConnectionId;
  private final String settlementAccount;
  private final String tpspId;
  private final String tpspName;
  private final String networkName;
  private final String outputChannel;
  private final String status;
  private final List<Double> debitCapLimitThresholds;
  private final BigDecimal debitCapLimit;
  private final List<OutputFlow> outputFlow;
  private final ZonedDateTime updatedAt;
  private final String updatedBy;
}
