package com.vocalink.crossproduct.infrastructure.bps.file;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSFileReference {

  private final String status;
  private final boolean hasReason;
  private final List<String> reasonCodes;
  private final String enquiryType;

  @JsonCreator
  public BPSFileReference(@JsonProperty(value = "status", required = true) String status,
      @JsonProperty(value = "hasReason", required = true) boolean hasReason,
      @JsonProperty(value = "enquiryType", required = true) String enquiryType,
      @JsonProperty("reasonCodes") @JsonInclude(Include.NON_EMPTY) List<String> reasonCodes) {
    this.status = status;
    this.hasReason = hasReason;
    this.reasonCodes = reasonCodes;
    this.enquiryType = enquiryType;
  }
}
