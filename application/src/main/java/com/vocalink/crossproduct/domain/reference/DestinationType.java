package com.vocalink.crossproduct.domain.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DestinationType {
  ALERTS("bps-config.destination.alerts-participant-types"),
  FILE_ENQ("bps-config.destination.file-enquiries-participant-types"),
  BATCH_ENQ("bps-config.destination.batch-enquiries-participant-types"),
  TRX_ENQ("bps-config.destination.transaction-enquiries-participant-types"),
  SETTLEMENT_ENQ("bps-config.destination.settlement-enquiry-participant-types"),
  AUDIT("bps-config.destination.audit-log-participant-types"),
  REPORTS("bps-config.destination.reports-participant-types"),
  APPROVALS("bps-config.destination.approvals-participant-types"),
  BROADCASTS("bps-config.destination.broadcasts-participant-types");

  final String propertyKey;
}
