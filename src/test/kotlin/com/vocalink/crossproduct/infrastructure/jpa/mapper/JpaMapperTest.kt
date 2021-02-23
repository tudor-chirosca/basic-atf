package com.vocalink.crossproduct.infrastructure.jpa.mapper

import com.vocalink.crossproduct.domain.role.Role
import com.vocalink.crossproduct.infrastructure.jpa.mapper.JpaMapper.JPAMAPPER
import com.vocalink.crossproduct.infrastructure.jpa.permission.UIPermissionJpa
import com.vocalink.crossproduct.infrastructure.jpa.role.RoleJpa
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class JpaMapperTest {

    @Test
    fun `should map RoleJpa to Role entity`() {
        val permissionId = UUID.randomUUID().toString()
        val function = Role.Function.READ_ONLY
        val roleJpa = RoleJpa.builder().id(permissionId).function(function).build()

        val roleEntity = JPAMAPPER.toEntity(roleJpa)

        assertThat(roleEntity.function).isEqualTo(roleJpa.function)
        assertThat(roleEntity.id).isEqualTo(roleJpa.id)
    }

    @Test
    fun `should map UIPermissionJpa to UIPermission entity`() {
        val permissionId = UUID.randomUUID().toString()
        val key = "test-permission"
        val permissionJpa = UIPermissionJpa.builder().id(permissionId).key(key).build()

        val permissionEntity = JPAMAPPER.toEntity(permissionJpa)

        assertThat(permissionEntity.id).isEqualTo(permissionJpa.id)
        assertThat(permissionEntity.key).isEqualTo(permissionJpa.key)
    }
}
