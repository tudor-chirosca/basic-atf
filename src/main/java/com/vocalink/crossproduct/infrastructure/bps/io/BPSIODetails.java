package com.vocalink.crossproduct.infrastructure.bps.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSIODetails {

  private final BPSIODataDetails files;
  private final List<BPSIOSummary> batches;
  private final List<BPSIOSummary> transactions;

  @JsonCreator
  public BPSIODetails(
      @JsonProperty(value = "files") BPSIODataDetails files,
      @JsonProperty(value = "batches") List<BPSIOSummary> batches,
      @JsonProperty(value = "transactions") List<BPSIOSummary> transactions) {
    this.files = files;
    this.batches = batches;
    this.transactions = transactions;
  }

  @Getter
  public static class BPSIODataDetails {

    private final Integer submitted;
    private final Integer accepted;
    private final String rejected;

    @JsonCreator
    public BPSIODataDetails(
        @JsonProperty(value = "submitted") Integer submitted,
        @JsonProperty(value = "accepted") Integer accepted,
        @JsonProperty(value = "rejected") String rejected) {
      this.submitted = submitted;
      this.rejected = rejected;
      this.accepted = accepted;
    }
  }
}
