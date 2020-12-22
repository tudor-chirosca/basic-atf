package com.vocalink.crossproduct.ui.dto.participant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vocalink.crossproduct.shared.participant.ParticipantStatus;
import com.vocalink.crossproduct.shared.participant.ParticipantType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ParticipantDto {

  private final String id;
  private final String bic;
  private final String name;
  private final String fundingBic;
  private final ParticipantStatus status;
  @JsonInclude(Include.NON_EMPTY)
  private final LocalDateTime suspendedTime;
  private final ParticipantType participantType;

  @JsonProperty("participantType")
  public String getParticipantType() {
    return participantType.getDescription();
  }

}


