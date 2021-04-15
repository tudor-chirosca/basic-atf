package com.vocalink.crossproduct.domain.audit;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserActivity {

  private final UUID id;
  private final String name;
}
