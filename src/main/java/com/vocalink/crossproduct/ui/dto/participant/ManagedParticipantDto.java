package com.vocalink.crossproduct.ui.dto.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ManagedParticipantDto {

  private final String bic;
  private final String fundingBic;
  private final String id;
  private final String name;
  private final ParticipantStatus status;
  private final ZonedDateTime suspendedTime;
  private final ParticipantType participantType;
  private final String organizationId;
  private final Boolean hasActiveSuspensionRequests = Boolean.FALSE;
  @JsonInclude(Include.NON_EMPTY)
  private final String tpspName;
  @JsonInclude(Include.NON_EMPTY)
  private final String tpspId;
  @JsonInclude(Include.NON_EMPTY)
  private final List<ParticipantReferenceDto> fundedParticipants;
  private final int fundedParticipantsCount;
}
