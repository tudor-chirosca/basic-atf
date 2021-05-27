package com.vocalink.crossproduct.domain.approval;

import static com.vocalink.crossproduct.ui.aspects.EventType.APPROVE_REQUEST;
import static com.vocalink.crossproduct.ui.aspects.EventType.REJECT_REQUEST;

import com.vocalink.crossproduct.ui.aspects.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalConfirmationType {

  APPROVE(APPROVE_REQUEST),
  REJECT(REJECT_REQUEST);

  private final EventType eventType;
}
