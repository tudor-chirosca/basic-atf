package com.vocalink.crossproduct.domain.participant;

import com.vocalink.crossproduct.domain.routing.RoutingRecord;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class Participant {

  private final String id;
  private final String bic;
  private final String name;
  private final String fundingBic;
  private final ParticipantStatus status;
  private final ZonedDateTime suspendedTime;
  private final ParticipantType participantType;
  private final String schemeCode;
  private final String organizationId;
  private final String tpspName;
  private final String tpspId;
  @Setter
  private List<Participant> fundedParticipants;
  @Setter
  private Integer fundedParticipantsCount;
  @Setter
  private List<RoutingRecord> reachableBics;
}
