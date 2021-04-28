package com.vocalink.crossproduct.domain.role;

import java.util.List;

public interface RoleRepository {

  List<Role> findByFunctions(List<Role.Function> functions);
}
