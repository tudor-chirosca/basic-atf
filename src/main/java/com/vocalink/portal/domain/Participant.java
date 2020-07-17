package com.vocalink.portal.domain;

import java.time.LocalDateTime;
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
  private String bic;
  private String name;
  private ParticipantStatus status;
  private LocalDateTime suspendedTime;
}


