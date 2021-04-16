package com.vocalink.crossproduct.ui.aspects;

import com.vocalink.crossproduct.ui.dto.batch.BatchEnquirySearchRequest;
import com.vocalink.crossproduct.ui.dto.file.FileEnquirySearchRequest;
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
  SETTL_ENQUIRY(Object.class),
  SETTL_DETAILS(Object.class),
  AUDIT_LOG_ENQUIRY(Object.class),
  AUDIT_LOG_EVENT_DETAILS(Object.class),
  VIEW_ALERTS(Object.class),
  SEND_BROADCAST(Object.class),
  VIEW_SENT_BROADCAST(Object.class),
  VIEW_REPORTS(Object.class),
  DOWNLOAD_REPORT(Object.class),
  VIEW_PARTICIPANT_MNG_LIST(Object.class),
  VIEW_PARTICIPANT_MNG_DETAILS(Object.class),
  VIEW_APPROVAL_DASHBOARD(Object.class),
  VIEW_APPROVAL_REQ(Object.class),
  VIEW_SCHEME_SETTL_DASHBOARD(Object.class),
  VIEW_PTT_SETTL_DASHBOARD(Object.class),
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
