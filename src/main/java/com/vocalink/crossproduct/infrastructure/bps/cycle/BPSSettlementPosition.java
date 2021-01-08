package com.vocalink.crossproduct.infrastructure.bps.cycle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BPSSettlementPosition {

  private final String participantId;
  private final String cycleId;
  private final String currency;
  private final BPSPayment paymentSent;
  private final BPSPayment paymentReceived;
  private final BPSPayment returnSent;
  private final BPSPayment returnReceived;
  private final BPSAmount netPositionAmount;

  @JsonCreator
  public BPSSettlementPosition(@JsonProperty(value = "participantId") String participantId,
      @JsonProperty(value = "cycleId") String cycleId,
      @JsonProperty(value = "currency") String currency,
      @JsonProperty(value = "paymentSent") BPSPayment paymentSent,
      @JsonProperty(value = "paymentReceived") BPSPayment paymentReceived,
      @JsonProperty(value = "returnSent") BPSPayment returnSent,
      @JsonProperty(value = "returnReceived") BPSPayment returnReceived,
      @JsonProperty(value = "netPositionAmount") BPSAmount netPositionAmount) {
    this.participantId = participantId;
    this.cycleId = cycleId;
    this.currency = currency;
    this.paymentSent = paymentSent;
    this.paymentReceived = paymentReceived;
    this.returnSent = returnSent;
    this.returnReceived = returnReceived;
    this.netPositionAmount = netPositionAmount;
  }
}
