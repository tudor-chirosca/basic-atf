package com.vocalink.crossproduct.infrastructure.jpa.role

import com.vocalink.crossproduct.domain.role.Role
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
open class RoleRepositoryJpaTest @Autowired constructor(var entityManager: TestEntityManager,
                                                        var roleRepositoryJpa: RoleRepositoryJpa) {

    @Test
    fun `should return roles associated to role functions`() {
        val roleFunction = Role.Function.READ_ONLY
        val readOnlyRole = RoleJpa.builder()
                .id(UUID.randomUUID().toString())
                .function(roleFunction)
                .build()
        entityManager.persist(readOnlyRole)
        entityManager.flush()

        val roles = roleRepositoryJpa
                .findByFunctionIn(
                        singletonList(Role.Function.READ_ONLY))

        assertThat(roles.contains(readOnlyRole));
    }
}
