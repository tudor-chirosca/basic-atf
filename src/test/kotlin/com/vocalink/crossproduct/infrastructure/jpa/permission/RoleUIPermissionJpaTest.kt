package com.vocalink.crossproduct.infrastructure.jpa.permission

import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.role.Role
import com.vocalink.crossproduct.infrastructure.jpa.role.RoleJpa
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals

open class RoleUIPermissionJpaTest {

    @Test
    fun `should return attributes from composite entity`() {
        val participantType = ParticipantType.DIRECT
        val uiPermissionJpa = UIPermissionJpa.builder()
                .id(UUID.randomUUID().toString())
                .key("read.test-permission")
                .build()
        val roleJpa = RoleJpa.builder()
                .id(UUID.randomUUID().toString())
                .function(Role.Function.READ_ONLY)
                .build()
        val roleUIPermission = RoleUIPermissionJpa(roleJpa, uiPermissionJpa, participantType)

        val uiPermission = roleUIPermission.uiPermission
        val role = roleUIPermission.role
        val type = roleUIPermission.participantType

        assertThat(uiPermission).isEqualTo(uiPermissionJpa)
        assertThat(role).isEqualTo(roleJpa)
        assertThat(type).isEqualTo(participantType)
    }

    @Test
    fun `should verify equality`(){
        val participantType = ParticipantType.DIRECT
        val uiPermissionJpa = UIPermissionJpa.builder()
                .id(UUID.randomUUID().toString())
                .key("read.test-permission")
                .build()
        val roleJpa = RoleJpa.builder()
                .id(UUID.randomUUID().toString())
                .function(Role.Function.READ_ONLY)
                .build()
        val roleUIPermission = RoleUIPermissionJpa(roleJpa, uiPermissionJpa, participantType)
        val roleUIPermissionOther = RoleUIPermissionJpa(roleJpa, uiPermissionJpa, participantType)

        assertEquals(roleUIPermission, roleUIPermissionOther)
    }
}
