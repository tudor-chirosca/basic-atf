package com.vocalink.crossproduct.infrastructure.bps.reference;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BPSReasonCodeReference {

  private final List<BPSValidation> validations;

  public BPSReasonCodeReference(@JsonProperty("validations") List<BPSValidation> validations) {
    this.validations = validations;
  }

  @Getter
  public static class BPSValidation {

    private final String validationLevel;
    private final List<BPSReasonCode> reasonCodes;

    public BPSValidation(@JsonProperty("validationLevel") String validationLevel,
        @JsonProperty("reasonCodes") List<BPSReasonCode> reasonCodes) {
      this.validationLevel = validationLevel;
      this.reasonCodes = reasonCodes;
    }
  }

  @Getter
  public static class BPSReasonCode {

    private final String reasonCode;
    private final String description;
    private final Boolean active;

    public BPSReasonCode(@JsonProperty("reasonCode") String reasonCode,
        @JsonProperty("description") String description,
        @JsonProperty("active") Boolean active) {
      this.reasonCode = reasonCode;
      this.description = description;
      this.active = active;
    }
  }
}
