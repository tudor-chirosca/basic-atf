package com.vocalink.crossproduct.infrastructure.jpa.activities;

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@DataJpaTest
@ActiveProfiles("test")
open class AuditDetailsActivityRepositoryJpaTest @Autowired constructor(var userActivityRepository: UserActivityRepositoryJpa) {

    companion object {
        private val details = UserActivityJpa.builder()
                .id(UUID.randomUUID())
                .name("name")
                .build()
    }

    @Test
    fun `should find entity with generated UUID as id`() {
        userActivityRepository.save(details)

        val all = userActivityRepository.findAll()

        assertThat(all).isNotNull
        assertThat(all[0].id).isEqualTo(details.id)
    }
}