package com.vocalink.crossproduct.infrastructure.bps.transaction;

import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import com.vocalink.crossproduct.infrastructure.bps.cycle.BPSAmount;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BPSTransactionEnquirySearchRequest {

  private final ZonedDateTime createdDateFrom;
  private final ZonedDateTime createdDateTo;
  private final ZonedDateTime cycleDay;
  private final String cycleName;
  private final String messageDirection;
  private final String sendingParticipant;
  private final String receivingParticipant;
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
