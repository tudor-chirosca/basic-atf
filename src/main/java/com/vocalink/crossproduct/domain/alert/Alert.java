package com.vocalink.crossproduct.domain.alert;

import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Alert {

  private final Integer alertId;
  private final AlertPriorityType priority;
  private final ZonedDateTime dateRaised;
  private final String type;
  private final List<ParticipantReference> entities;
}
