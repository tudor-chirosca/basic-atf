package com.vocalink.crossproduct.ui.dto.audit;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UserDetailsDto {

  private final String username;
  private final String fullName;
}
