package com.vocalink.crossproduct.infrastructure.bps.reference;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BPSOutputFlowReference {

  private final String messageType;
  private final Boolean editable;
  private final Integer outputVolumeMin;
  private final Integer outputVolumeMax;
  private final Integer outputTimeMin;
  private final Integer outputTimeMax;

  public BPSOutputFlowReference(
      @JsonProperty(value = "messageType", required = true) String messageType,
      @JsonProperty(value = "editable", required = true) Boolean editable,
      @JsonProperty(value = "outputVolumeMin", required = true) Integer outputVolumeMin,
      @JsonProperty(value = "outputVolumeMax", required = true) Integer outputVolumeMax,
      @JsonProperty(value = "outputTimeMin") Integer outputTimeMin,
      @JsonProperty(value = "outputTimeMax") Integer outputTimeMax) {
    this.messageType = messageType;
    this.editable = editable;
    this.outputVolumeMin = outputVolumeMin;
    this.outputVolumeMax = outputVolumeMax;
    this.outputTimeMin = outputTimeMin;
    this.outputTimeMax = outputTimeMax;
  }
}
