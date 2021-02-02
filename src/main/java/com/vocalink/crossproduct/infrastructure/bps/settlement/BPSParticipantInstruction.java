package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
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
      @JsonProperty(value = "cycleId") String cycleId,
      @JsonProperty(value = "participantId") String participantId,
      @JsonProperty(value = "reference") String reference,
      @JsonProperty(value = "status") String status,
      @JsonProperty(value = "counterpartyId") String counterpartyId,
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
