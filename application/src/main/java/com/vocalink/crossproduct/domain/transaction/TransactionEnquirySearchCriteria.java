package com.vocalink.crossproduct.domain.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TransactionEnquirySearchCriteria {

  private final int offset;
  private final int limit;
  private final List<String> sort;
  private final LocalDate dateFrom;
  private final LocalDate dateTo;
  private final LocalDate cycleDay;
  private final String cycleName;
  private final String messageDirection;
  private final String messageType;
  private final String sendingBic;
  private final String receivingBic;
  private final String status;
  private final String reasonCode;
  private final String id;
  private final String sendingAccount;
  private final String receivingAccount;
  private final LocalDate valueDate;
  private final BigDecimal txnFrom;
  private final BigDecimal txnTo;
}
