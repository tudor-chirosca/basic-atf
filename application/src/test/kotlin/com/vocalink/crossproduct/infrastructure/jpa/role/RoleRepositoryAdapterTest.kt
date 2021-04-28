package com.vocalink.crossproduct.infrastructure.jpa.role

import com.vocalink.crossproduct.domain.role.Role
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyList
import java.util.UUID
import java.util.Collections.singletonList

open class RoleRepositoryAdapterTest {

    private val roleRepositoryJpa = mock(RoleRepositoryJpa::class.java)!!
    private val roleRepositoryAdapter = RoleRepositoryAdapter(roleRepositoryJpa)

    @Test
    fun `should return Role domain object from existing JPA entity`() {
        val permissionId = UUID.randomUUID().toString()
        val function = Role.Function.READ_ONLY
        val readOnlyRoleJpa = RoleJpa.builder().id(permissionId).function(function).build()
        val readOnlyRoleDomain = Role.builder().id(permissionId).function(function).build()
        `when`(roleRepositoryJpa.findByFunctionIn(anyList()))
                .thenReturn(singletonList(readOnlyRoleJpa))

        val roles = roleRepositoryAdapter.findByFunctions(singletonList(Role.Function.READ_ONLY))

        assertThat(roles.contains(readOnlyRoleDomain))
    }
}
