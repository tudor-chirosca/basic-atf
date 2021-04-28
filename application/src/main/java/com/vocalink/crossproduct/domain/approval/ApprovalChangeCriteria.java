package com.vocalink.crossproduct.domain.approval;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApprovalChangeCriteria {

  private final ApprovalRequestType requestType;
  private final Map<String, Object> requestedChange;
  private final String notes;
}
