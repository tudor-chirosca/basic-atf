package com.vocalink.crossproduct.domain.error;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(Include.NON_NULL)
public class GatewayError {
  @Getter(onMethod_ = { @JsonGetter("Source") })
  private final String source;
  @Getter(onMethod_ = { @JsonGetter("ReasonCode") })
  private final String reasonCode;
  @Getter(onMethod_ = { @JsonGetter("Description") })
  private final String description;
  @Getter(onMethod_ = { @JsonGetter("Recoverable") })
  private final boolean recoverable;

  /**
   * Always null, present for backward compatibility
   */
  @Getter(onMethod_ = { @JsonGetter("Details") })
  private final String details = null;
}
