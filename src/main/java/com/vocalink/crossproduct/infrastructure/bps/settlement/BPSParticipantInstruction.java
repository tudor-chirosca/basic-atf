package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class BPSParticipantInstruction {

  private final String cycleId;
  private final String participantId;
  private final String reference;
  private final String status;
  private final String counterpartyId;
  @JsonInclude(Include.NON_EMPTY)
  private final String settlementCounterpartyId;
  @JsonInclude(Include.NON_EMPTY)
  private final BigDecimal totalDebit;
  @JsonInclude(Include.NON_EMPTY)
  private final BigDecimal totalCredit;

  @JsonCreator
  public BPSParticipantInstruction(
      @JsonProperty(value = "cycleId", required = true) String cycleId,
      @JsonProperty(value = "participantId", required = true) String participantId,
      @JsonProperty(value = "reference", required = true) String reference,
      @JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "counterpartyId", required = true) String counterpartyId,
      @JsonProperty(value = "settlementCounterpartyId") String settlementCounterpartyId,
      @JsonProperty(value = "totalDebit") BigDecimal totalDebit,
      @JsonProperty(value = "totalCredit") BigDecimal totalCredit) {
    this.cycleId = cycleId;
    this.participantId = participantId;
    this.reference = reference;
    this.status = status;
    this.counterpartyId = counterpartyId;
    this.settlementCounterpartyId = settlementCounterpartyId;
    this.totalDebit = totalDebit;
    this.totalCredit = totalCredit;
  }
}
