package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSCycleStatus;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class BPSSettlementDetails {

  private final String schemeParticipantIdentifier;
  private final String settlementBank;
  private final String cycleId;
  private final ZonedDateTime settlementCycleDate;
  private final BPSCycleStatus status;
  private final String settlementInstructionReference;
  private final String statusDetail;
  private final String counterParty;
  private final String counterPartySettlement;
  private final BPSAmount totalAmountDebited;
  private final BPSAmount totalAmountCredited;

  @JsonCreator
  public BPSSettlementDetails(
      @JsonProperty(value = "schemeParticipantIdentifier") String schemeParticipantIdentifier,
      @JsonProperty(value = "settlementBank") String settlementBank,
      @JsonProperty(value = "cycleId") String cycleId,
      @JsonProperty(value = "settlementCycleDate") ZonedDateTime settlementCycleDate,
      @JsonProperty(value = "status") BPSCycleStatus status,
      @JsonProperty(value = "settlementInstructionReference") String settlementInstructionReference,
      @JsonProperty(value = "statusDetail") String statusDetail,
      @JsonProperty(value = "counterParty") String counterParty,
      @JsonProperty(value = "counterPartySettlement") String counterPartySettlement,
      @JsonProperty(value = "totalAmountDebited") BPSAmount totalAmountDebited,
      @JsonProperty(value = "totalAmountCredited") BPSAmount totalAmountCredited) {
    this.schemeParticipantIdentifier = schemeParticipantIdentifier;
    this.settlementBank = settlementBank;
    this.cycleId = cycleId;
    this.settlementCycleDate = settlementCycleDate;
    this.status = status;
    this.settlementInstructionReference = settlementInstructionReference;
    this.statusDetail = statusDetail;
    this.counterParty = counterParty;
    this.counterPartySettlement = counterPartySettlement;
    this.totalAmountDebited = totalAmountDebited;
    this.totalAmountCredited = totalAmountCredited;
  }
}
