package com.vocalink.crossproduct.domain.approval;

import static com.vocalink.crossproduct.ui.aspects.EventType.AMEND_PARTICIPANT_CONFIG;
import static com.vocalink.crossproduct.ui.aspects.EventType.REQ_BATCH_CANCELLATION;
import static com.vocalink.crossproduct.ui.aspects.EventType.REQ_TRANSACTION_CANCELLATION;
import static com.vocalink.crossproduct.ui.aspects.EventType.SUSPEND_PARTICIPANT;
import static com.vocalink.crossproduct.ui.aspects.EventType.UNSUSPEND_PARTICIPANT;
import static java.util.Arrays.asList;

import com.vocalink.crossproduct.ui.aspects.EventType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApprovalRequestType {

  PARTICIPANT_SUSPEND(SUSPEND_PARTICIPANT, asList("P27-SEK", "SAMA-SAR")),
  PARTICIPANT_UNSUSPEND(UNSUSPEND_PARTICIPANT, asList("P27-SEK", "SAMA-SAR")),
  CONFIG_CHANGE(AMEND_PARTICIPANT_CONFIG, asList("P27-SEK", "SAMA-SAR")),
  BATCH_CANCELLATION(REQ_BATCH_CANCELLATION, asList("P27-SEK", "SAMA-SAR")),
  TRANSACTION_CANCELLATION(REQ_TRANSACTION_CANCELLATION, asList("SAMA-SAR")),
  FILE_UPLOAD(null, asList("SAMA-SAR"));

  private final EventType eventType;
  private final List<String> supportedEnvironments;
}
