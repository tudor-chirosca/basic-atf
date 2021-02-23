package com.vocalink.crossproduct.infrastructure.jpa.permission;

import static com.vocalink.crossproduct.infrastructure.jpa.mapper.JpaMapper.JPAMAPPER;

import com.vocalink.crossproduct.domain.participant.ParticipantType;
import com.vocalink.crossproduct.domain.permission.UIPermission;
import com.vocalink.crossproduct.domain.permission.UIPermissionRepository;
import com.vocalink.crossproduct.domain.role.Role;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UIPermissionRepositoryAdapter implements UIPermissionRepository {

  private final UIPermissionRepositoryJpa uiPermissionRepositoryJPA;

  @Override
  public List<UIPermission> findByRolesAndParticipantType(List<Role.Function> functions,
      ParticipantType participantType) {
    return uiPermissionRepositoryJPA.findByRolesAndParticipantType(functions, participantType)
        .stream()
        .map(JPAMAPPER::toEntity)
        .collect(Collectors.toList());
  }
}
