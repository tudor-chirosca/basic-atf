package com.vocalink.crossproduct.infrastructure.jpa.permission

import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.permission.UIPermission
import com.vocalink.crossproduct.domain.role.Role
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.any
import java.util.UUID
import java.util.Collections.singletonList

open class UIPermissionRepositoryAdapterTest {

    private val uiPermissionRepositoryJpa = mock(UIPermissionRepositoryJpa::class.java)!!
    private val uiPermissionRepositoryAdapter = UIPermissionRepositoryAdapter(uiPermissionRepositoryJpa)

    @Test
    fun `should return UIPermission domain object from JPA entity`() {
        val permissionId = UUID.randomUUID().toString()
        val key = "test-permission"
        val readPermissionJPA = UIPermissionJpa.builder().id(permissionId).key(key).build()
        val readPermissionDomain = UIPermission.builder().id(permissionId).key(key).build()
        `when`(uiPermissionRepositoryJpa.findByRolesAndParticipantType(anyList(),
                any(ParticipantType::class.java))).thenReturn(singletonList(readPermissionJPA))

        val result = uiPermissionRepositoryAdapter.findByRolesAndParticipantType(
                singletonList(Role.Function.READ_ONLY), ParticipantType.DIRECT)

        assertThat(result.contains(readPermissionDomain))
    }

}
