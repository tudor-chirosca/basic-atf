package com.vocalink.crossproduct.domain;

import com.vocalink.crossproduct.ui.dto.ParticipantDto;
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
public class Participant {

  private String id;
  private String bic;
  private String name;
  private String fundingBic;
  private ParticipantStatus status;
  private LocalDateTime suspendedTime;

  public ParticipantDto toDto() {
    return ParticipantDto
        .builder()
        .id(id)
        .bic(bic)
        .name(name)
        .status(status)
        .suspendedTime(suspendedTime)
        .build();
  }
}


