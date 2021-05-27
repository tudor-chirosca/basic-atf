package com.vocalink.crossproduct.domain.approval;

import com.vocalink.crossproduct.ui.aspects.EventType;

import static com.vocalink.crossproduct.ui.aspects.EventType.AMEND_PTT_CONFIG;
import static com.vocalink.crossproduct.ui.aspects.EventType.REQ_BATCH_CANCELLATION;
import static com.vocalink.crossproduct.ui.aspects.EventType.SUSPEND_PTT;
import static com.vocalink.crossproduct.ui.aspects.EventType.UNSUSPEND_PTT;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalRequestType {

  PARTICIPANT_SUSPEND(SUSPEND_PTT),
  PARTICIPANT_UNSUSPEND(UNSUSPEND_PTT),
  CONFIG_CHANGE(AMEND_PTT_CONFIG),
  BATCH_CANCELLATION(REQ_BATCH_CANCELLATION);

  private final EventType eventType;
}
