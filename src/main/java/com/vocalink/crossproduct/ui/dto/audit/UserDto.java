package com.vocalink.crossproduct.ui.dto.audit;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UserDto {

  private final String id;
  private final String name;
  private final String participantName;
}
