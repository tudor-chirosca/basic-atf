package com.vocalink.crossproduct.infrastructure.bps.settlement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.infrastructure.bps.BPSSortingQuery;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BPSSettlementEnquiryRequest {

  private ZonedDateTime dateFrom;
  private ZonedDateTime dateTo;
  private String sessionInstanceId;
  @JsonProperty("participant")
  private List<BPSParticipantWrapper> participants;
  private List<BPSSortingQuery> sortingOrder;

  @Getter
  @Setter
  @AllArgsConstructor
  public static class BPSParticipantWrapper {

    private String participantId;
  }
}
