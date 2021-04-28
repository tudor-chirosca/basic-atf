package com.vocalink.crossproduct.infrastructure.jpa.role;

import static com.vocalink.crossproduct.infrastructure.jpa.mapper.JpaMapper.JPAMAPPER;
import static java.util.stream.Collectors.toList;

import com.vocalink.crossproduct.domain.role.Role;
import com.vocalink.crossproduct.domain.role.RoleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {
  private final RoleRepositoryJpa roleRepositoryJPA;

  @Override
  public List<Role> findByFunctions(List<Role.Function> functions) {
    return roleRepositoryJPA.findByFunctionIn(functions)
        .stream()
        .map(JPAMAPPER::toEntity)
        .collect(toList());
  }
}
