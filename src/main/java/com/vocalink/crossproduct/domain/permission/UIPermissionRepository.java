package com.vocalink.crossproduct.domain.permission;

import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.role.Role;
import java.util.List;

public interface UIPermissionRepository {

  List<UIPermission> findByRolesAndParticipantType(List<Role.Function> functions,
      ParticipantType participantType);
}
