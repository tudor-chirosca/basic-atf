package com.vocalink.crossproduct.domain.alert;

import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Alert {

  private final Integer alertId;
  private final String priority;
  private final LocalDateTime dateRaised;
  private final String type;
  private final List<ParticipantReference> entities;
}
