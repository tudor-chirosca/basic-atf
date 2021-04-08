package com.vocalink.crossproduct.ui.dto.permission;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoDto {

  private final UUID userId;
  private final String name;
}
