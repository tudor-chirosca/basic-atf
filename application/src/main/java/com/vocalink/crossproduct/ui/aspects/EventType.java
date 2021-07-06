package com.vocalink.crossproduct.ui.aspects;

import static java.util.Arrays.stream;

import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.approval.ApprovalSearchRequest;
import com.vocalink.crossproduct.ui.dto.audit.AuditRequestParams;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastRequest;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastsSearchParameters;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementDashboardRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {

  FILE_ENQUIRY(FileEnquirySearchRequest.class),
  FILE_DETAILS(String.class),
  BATCH_ENQUIRY(BatchEnquirySearchRequest.class),
  BATCH_DETAILS(String.class),
  TRANSACTION_ENQUIRY(TransactionEnquirySearchRequest.class),
  TRANSACTION_DETAILS(String.class),
  SETTL_ENQUIRY(SettlementEnquiryRequest.class),
  SETTL_DETAILS(ParticipantSettlementRequest.class),
  AUDIT_LOG_ENQUIRY(AuditRequestParams.class),
  AUDIT_LOG_EVENT_DETAILS(String.class),
  VIEW_ALERTS(AlertSearchRequest.class),
  SEND_BROADCAST(BroadcastRequest.class),
  VIEW_SENT_BROADCAST(BroadcastsSearchParameters.class),
  VIEW_REPORTS(ReportsSearchRequest.class),
  DOWNLOAD_REPORT(String.class),
  VIEW_PARTICIPANT_MNG_LIST(ManagedParticipantsSearchRequest.class),
  VIEW_PARTICIPANT_MNG_DETAILS(String.class),
  VIEW_APPROVAL_DASHBOARD(ApprovalSearchRequest.class),
  VIEW_APPROVAL_REQ(Object.class),
  VIEW_SETTLEMENT_DASHBOARD(SettlementDashboardRequest.class),
  VIEW_SETTLEMENT_DASHBOARD_BY_MESSAGE_TYPE(String.class),
  VIEW_SCHEME_IO_DASHBOARD(String.class),
  VIEW_PTT_IO_DASHBOARD(String.class),
  REQ_BATCH_CANCELLATION(Object.class),
  REQ_TRANSACTION_CANCELLATION(Object.class),
  SUSPEND_PARTICIPANT(Object.class),
  UNSUSPEND_PARTICIPANT(Object.class),
  AMEND_PARTICIPANT_CONFIG(Object.class),
  APPROVE_REQUEST(Object.class),
  REJECT_REQUEST(Object.class),
  UNKNOWN(Object.class),
  USER_SESSION_TIMEOUT(Object.class),
  USER_SESSION_START(Object.class),
  USER_SESSION_END(Object.class);

  private final Class<?> requestType;

  public static List<String> getEventsList() {
    return stream(EventType.values())
        .filter(e -> e!=UNKNOWN)
        .map(Enum::name)
        .collect(Collectors.toList());
  }
}
