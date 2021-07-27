package com.vocalink.crossproduct.domain.transaction;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionEnquirySearchCriteria {

  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final ZonedDateTime dateFrom;
  private final ZonedDateTime dateTo;
  private final String cycleId;
  private final String messageType;
  private final String sendingParticipant;
  private final String receivingParticipant;
  private final String debtor;
  private final String creditor;
  private final String status;
  private final String reasonCode;
  private final String id;
  private final String sendingAccount;
  private final String receivingAccount;
  private final ZonedDateTime valueDate;
  private final BigDecimal txnFrom;
  private final BigDecimal txnTo;
  private final OutputType outputType;
}
