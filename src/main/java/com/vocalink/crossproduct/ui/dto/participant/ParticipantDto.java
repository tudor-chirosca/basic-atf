package com.vocalink.crossproduct.ui.dto.participant;

import com.vocalink.crossproduct.domain.participant.ParticipantStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {

  private String id;
  private String bic;
  private String name;
  private String fundingBic;
  private ParticipantStatus status;
  private LocalDateTime suspendedTime;
}


