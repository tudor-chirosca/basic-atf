package com.vocalink.crossproduct.ui.dto.alert;

import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AlertDto {

  private final Integer alertId;
  private final String priority;
  private final ZonedDateTime dateRaised;
  private final String type;
  private final List<ParticipantReferenceDto> entities;
}
