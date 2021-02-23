package com.vocalink.crossproduct.infrastructure.jpa.permission

import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.role.Role
import com.vocalink.crossproduct.infrastructure.jpa.role.RoleJpa
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.util.Collections.singletonList
import java.util.UUID

@DataJpaTest
@ActiveProfiles("test")
open class UIPermissionRepositoryJpaTest @Autowired constructor(var entityManager: TestEntityManager,
                                                                var uiPermissionRepositoryJpa: UIPermissionRepositoryJpa) {

    @Test
    fun `should return UI permissions from associated role and participation type`() {
        val readSchemePermission = UIPermissionJpa.builder()
                .id(UUID.randomUUID().toString())
                .key("read.test-permission")
                .build()
        val readOnlyRole = RoleJpa.builder()
                .id(UUID.randomUUID().toString())
                .function(Role.Function.READ_ONLY)
                .build()
        val schemeRoleAndPermission = RoleUIPermissionJpa(readOnlyRole, readSchemePermission,
                ParticipantType.SCHEME_OPERATOR)
        entityManager.persist(readSchemePermission);
        entityManager.persist(readOnlyRole)
        entityManager.persist(schemeRoleAndPermission)
        entityManager.flush()

        val permissions = uiPermissionRepositoryJpa
                .findByRolesAndParticipantType(
                        singletonList(Role.Function.READ_ONLY),
                        ParticipantType.SCHEME_OPERATOR
                )

        assertThat(permissions.contains(readSchemePermission));
    }
}
