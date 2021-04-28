package com.vocalink.crossproduct.domain.broadcasts;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Broadcast {

  private final ZonedDateTime createdAt;
  private final String broadcastId;
  private final String message;
  private final List<String> recipients;
}
