package com.vocalink.crossproduct.domain.approval;

import com.vocalink.crossproduct.ui.aspects.EventType;

import static com.vocalink.crossproduct.ui.aspects.EventType.AMEND_PARTICIPANT_CONFIG;
import static com.vocalink.crossproduct.ui.aspects.EventType.REQ_BATCH_CANCELLATION;
import static com.vocalink.crossproduct.ui.aspects.EventType.REQ_TRANSACTION_CANCELLATION;
import static com.vocalink.crossproduct.ui.aspects.EventType.SUSPEND_PARTICIPANT;
import static com.vocalink.crossproduct.ui.aspects.EventType.UNSUSPEND_PARTICIPANT;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalRequestType {

  PARTICIPANT_SUSPEND(SUSPEND_PARTICIPANT),
  PARTICIPANT_UNSUSPEND(UNSUSPEND_PARTICIPANT),
  CONFIG_CHANGE(AMEND_PARTICIPANT_CONFIG),
  BATCH_CANCELLATION(REQ_BATCH_CANCELLATION),
  TRANSACTION_CANCELLATION(REQ_TRANSACTION_CANCELLATION);

  private final EventType eventType;
}
