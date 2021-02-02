package com.vocalink.crossproduct.ui.dto.broadcasts;

import com.vocalink.crossproduct.domain.reference.ParticipantReference;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class BroadcastDto {

  private final ZonedDateTime createdAt;
  private final String broadcastId;
  private final String message;
  @Setter
  private List<ParticipantReference> recipients;
}
