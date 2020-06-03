package com.vocalink.portal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Participant {

  private String id;
  private String name;
  private ParticipantStatus participantStatus;
}


