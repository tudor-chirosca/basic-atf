package com.vocalink.crossproduct.domain.participant;

import com.vocalink.crossproduct.shared.participant.ParticipantType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Participant {

  private final String id;
  private final String bic;
  private final String name;
  private final String fundingBic;
  private final ParticipantStatus status;
  private final LocalDateTime suspendedTime;
  private final ParticipantType participantType;
}
