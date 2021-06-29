package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class BPSTransactionEnquirySearchRequest {

  @Setter
  private ZonedDateTime createdDateFrom;
  @Setter
  private ZonedDateTime createdDateTo;
  private final String sendingParticipant;
  private final String receivingParticipant;
  private final String debtor;
  private final String creditor;
  private final String sessionInstanceId;
  private final String messageType;
  private final String status;
  private final String reasonCode;
  private final String instructionIdentifier;
  private final String sendingAccount;
  private final String receivingAccount;
  private final ZonedDateTime valueDate;
  private final BPSAmount transactionRangeFrom;
  private final BPSAmount transactionRangeTo;
  private final List<BPSSortingQuery> sortingOrder;
}
