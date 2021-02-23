package com.vocalink.crossproduct.infrastructure.jpa.permission;

import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.role.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UIPermissionRepositoryJpa extends JpaRepository<UIPermissionJpa, String> {

  @Query("select rp.id.uiPermission from RoleUIPermissionJpa rp where rp.id.role.function in (:roles) and rp.id.participantType = :participantType ")
  List<UIPermissionJpa> findByRolesAndParticipantType(List<Role.Function> roles,
                                                      ParticipantType participantType);
}
