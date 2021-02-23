package com.vocalink.crossproduct.domain.permission;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UIPermission {
  private final String id;
  private final String key;
}
