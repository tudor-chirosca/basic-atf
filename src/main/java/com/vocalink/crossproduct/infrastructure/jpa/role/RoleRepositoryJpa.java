package com.vocalink.crossproduct.infrastructure.jpa.role;

import com.vocalink.crossproduct.domain.role.Role.Function;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryJpa extends JpaRepository<RoleJpa, String> {
  List<RoleJpa> findByFunctionIn(List<Function> functions);
}
