package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class BPSIOTransactionSummary {

  private final String messageType;
  private final Integer submitted;
  private final Integer accepted;
  private final BPSAmount amountAccepted;
  private final String rejected;
  private final Integer output;
  private final BPSAmount amountOutput;

  public BPSIOTransactionSummary(
      @JsonProperty(value = "messageType", required = true) String messageType,
      @JsonProperty(value = "submitted", required = true) Integer submitted,
      @JsonProperty(value = "accepted", required = true) Integer accepted,
      @JsonProperty(value = "amountAccepted") BPSAmount amountAccepted,
      @JsonProperty(value = "rejected", required = true) String rejected,
      @JsonProperty(value = "output", required = true) Integer output,
      @JsonProperty(value = "amountOutput") BPSAmount amountOutput) {
    this.messageType = messageType;
    this.submitted = submitted;
    this.accepted = accepted;
    this.amountAccepted = amountAccepted;
    this.rejected = rejected;
    this.output = output;
    this.amountOutput = amountOutput;
  }

  @Getter
  public static class BPSAmount {

    private final BigDecimal amount;
    private final String currency;

    @JsonCreator
    public BPSAmount(
        @JsonProperty(value = "amount", required = true) BigDecimal amount,
        @JsonProperty(value = "currency", required = true) String currency) {
      this.amount = amount;
      this.currency = currency;
    }
  }
}
