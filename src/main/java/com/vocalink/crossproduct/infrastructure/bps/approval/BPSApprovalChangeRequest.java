package com.vocalink.crossproduct.infrastructure.bps.approval;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSApprovalChangeRequest {

  private final BPSApprovalRequestType requestType;
  private final Map<String, Object> requestedChange;
  private final String notes;
}
