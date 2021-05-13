package com.vocalink.crossproduct.infrastructure.bps.cycle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BPSSettlementPosition {

  public static final String CYCLE_ID_DATE_FORMAT = "yyyyMMdd";

  private final LocalDate settlementDate;
  private final String participantId;
  @Getter(AccessLevel.PRIVATE)
  private final String rawCycleId;
  private final String currency;
  private final BPSPayment paymentSent;
  private final BPSPayment paymentReceived;
  private final BPSPayment returnSent;
  private final BPSPayment returnReceived;
  private final BPSAmount netPositionAmount;

  @JsonCreator
  public BPSSettlementPosition(
      @JsonProperty(value = "settlementDate") LocalDate settlementDate,
      @JsonProperty(value = "participantId") String participantId,
      @JsonProperty(value = "cycleId") String rawCycleId,
      @JsonProperty(value = "currency") String currency,
      @JsonProperty(value = "paymentSent") BPSPayment paymentSent,
      @JsonProperty(value = "paymentReceived") BPSPayment paymentReceived,
      @JsonProperty(value = "returnSent") BPSPayment returnSent,
      @JsonProperty(value = "returnReceived") BPSPayment returnReceived,
      @JsonProperty(value = "netPositionAmount") BPSAmount netPositionAmount) {

    this.settlementDate = settlementDate;
    this.participantId = participantId;
    this.rawCycleId = rawCycleId;
    this.currency = currency;
    this.paymentSent = paymentSent;
    this.paymentReceived = paymentReceived;
    this.returnSent = returnSent;
    this.returnReceived = returnReceived;
    this.netPositionAmount = netPositionAmount;
  }

  public String getCycleId() {
    final DateTimeFormatter cycleIdDateFormatter = DateTimeFormatter.ofPattern(CYCLE_ID_DATE_FORMAT);
    if (settlementDate == null) {
      return rawCycleId;
    }
    return settlementDate.format(cycleIdDateFormatter) + rawCycleId;
  }
}
