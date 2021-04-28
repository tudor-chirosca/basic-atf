package com.vocalink.crossproduct.infrastructure.bps.reference;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSReasonCodeReference {

  private final String messageType;
  private final List<BPSReasonCodeValidation> validations;

  public BPSReasonCodeReference(
      @JsonProperty(value = "messageType") String messageType,
      @JsonProperty(value = "validations") List<BPSReasonCodeValidation> validations) {
    this.messageType = messageType;
    this.validations = validations;
  }

  @Getter
  public static class BPSReasonCodeValidation {

    private final BPSEnquiryType validationLevel;
    private final List<BPSReasonCode> reasonCodes;

    public BPSReasonCodeValidation(
        @JsonProperty(value = "validationLevel") BPSEnquiryType validationLevel,
        @JsonProperty(value = "reasonCodes") List<BPSReasonCode> reasonCodes) {
      this.validationLevel = validationLevel;
      this.reasonCodes = reasonCodes;
    }
  }

  @Getter
  public static class BPSReasonCode {

    private final String reasonCode;
    private final String description;
    private final Boolean active;

    public BPSReasonCode(
        @JsonProperty(value = "reasonCode") String reasonCode,
        @JsonProperty(value = "description") String description,
        @JsonProperty(value = "active") Boolean active) {
      this.reasonCode = reasonCode;
      this.description = description;
      this.active = active;
    }
  }
}
