package com.vocalink.crossproduct.ui.dto.permission;

import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentUserInfoDto {

  private final List<String> permissions;
  private final ParticipantDto participation;
  private final UserInfoDto user;
}
