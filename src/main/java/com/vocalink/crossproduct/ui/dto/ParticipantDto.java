package com.vocalink.crossproduct.ui.dto;

import com.vocalink.crossproduct.domain.ParticipantStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ParticipantDto {

  private String id;
  private String bic;
  private String name;
  private ParticipantStatus status;
  private LocalDateTime suspendedTime;
}


