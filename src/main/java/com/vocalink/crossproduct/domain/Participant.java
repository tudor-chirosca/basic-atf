package com.vocalink.crossproduct.domain;

import com.vocalink.crossproduct.ui.dto.ParticipantDto;
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


