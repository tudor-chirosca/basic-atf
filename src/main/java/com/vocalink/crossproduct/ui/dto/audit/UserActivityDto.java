package com.vocalink.crossproduct.ui.dto.audit;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserActivityDto {

  private final UUID id;
  private final String name;
  private final String description;
}
