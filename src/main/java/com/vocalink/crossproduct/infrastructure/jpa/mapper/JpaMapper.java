package com.vocalink.crossproduct.infrastructure.jpa.mapper;

import com.vocalink.crossproduct.domain.permission.UIPermission;
import com.vocalink.crossproduct.domain.role.Role;
import com.vocalink.crossproduct.infrastructure.jpa.permission.UIPermissionJpa;
import com.vocalink.crossproduct.infrastructure.jpa.role.RoleJpa;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface JpaMapper {

  JpaMapper JPAMAPPER = Mappers.getMapper(JpaMapper.class);

  UIPermission toEntity(UIPermissionJpa uiPermissionJpa);

  Role toEntity(RoleJpa roleJpa);
}
