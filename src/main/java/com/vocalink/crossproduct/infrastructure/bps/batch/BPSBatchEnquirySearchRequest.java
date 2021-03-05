package com.vocalink.crossproduct.infrastructure.bps.batch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class BPSBatchEnquirySearchRequest {

  @Setter
  private ZonedDateTime createdFromDate;
  @Setter
  private ZonedDateTime createdToDate;
  private final String sessionInstanceId;
  private final String messageDirection;
  private final String messageType;
  private final String sendingParticipant;
  private final String receivingParticipant;
  private final String status;
  private final String reasonCode;
  private final String identifier;
  @JsonInclude(Include.NON_EMPTY)
  private final List<BPSSortingQuery> sortingOrder;
}
