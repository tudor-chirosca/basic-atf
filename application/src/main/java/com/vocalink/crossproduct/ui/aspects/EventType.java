package com.vocalink.crossproduct.ui.aspects;

import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest;
import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastRequest;
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastsSearchParameters;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest;
import com.vocalink.crossproduct.ui.dto.report.ReportsSearchRequest;
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementRequest;
import com.vocalink.crossproduct.ui.dto.settlement.SettlementEnquiryRequest;
import com.vocalink.crossproduct.ui.dto.transaction.TransactionEnquirySearchRequest;

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
  AUDIT_LOG_ENQUIRY(Object.class),
  AUDIT_LOG_EVENT_DETAILS(Object.class),
  VIEW_ALERTS(AlertSearchRequest.class),
  SEND_BROADCAST(BroadcastRequest.class),
  VIEW_SENT_BROADCAST(BroadcastsSearchParameters.class),
  VIEW_REPORTS(ReportsSearchRequest.class),
  DOWNLOAD_REPORT(String.class),
  VIEW_PARTICIPANT_MNG_LIST(ManagedParticipantsSearchRequest.class),
  VIEW_PARTICIPANT_MNG_DETAILS(String.class),
  VIEW_APPROVAL_DASHBOARD(Object.class),
  VIEW_APPROVAL_REQ(Object.class),
  VIEW_SCHEME_SETTL_DASHBOARD(String.class),
  VIEW_PTT_SETTL_DASHBOARD(String.class),
  VIEW_SCHEME_IO_DASHBOARD(Object.class),
  VIEW_PTT_IO_DASHBOARD(Object.class),
  REQ_BATCH_CANCELLATION(Object.class),
  APPROVE_BATCH_CANCELLATION_REQ(Object.class),
  REJECT_BATCH_CANCELLATION_REQ(Object.class),
  SUSPEND_PTT(Object.class),
  APPROVE_SUSPEND(Object.class),
  REJECT_SUSPEND(Object.class),
  UNSUSPEND_PTT(Object.class),
  APPROVE_UNSUSPENSION(Object.class),
  REJECT_UNSUSPENSION(Object.class),
  AMEND_PTT_CONFIGURATION(Object.class),
  APPROVE_PTT_CONFIG_AMENDMENT(Object.class),
  REJECT_PTT_CONFIG_AMENDMENT(Object.class);

  private final Class<?> requestType;
}
