package com.vocalink.crossproduct.infrastructure.jpa.activities;

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.*
import javax.persistence.EntityManager


@DataJpaTest
@ActiveProfiles("test")
open class UserActivityAdapterTest @Autowired constructor(var entityManager: EntityManager, userActivityRepositoryJpa: UserActivityRepositoryJpa) {

    private val adapter = UserActivityAdapter(entityManager, userActivityRepositoryJpa)


    companion object {
        val uuid = UUID.randomUUID()
        private val uuids = listOf(uuid)
    }

    @BeforeEach
    fun init() {
        entityManager.persist(UserActivityJpa.builder().id(uuid).name("name").build())
    }

    @Test
    fun `should find activities using list of ids`() {
        val activitiesByIds = adapter.getActivitiesByIds(uuids)

        assertThat(activitiesByIds.size).isEqualTo(1)
        assertThat(uuid).isEqualTo(activitiesByIds[0].id)
    }

    @Test
    fun `should find activity by id`() {
        val activitiesById = adapter.getActivitiesById(uuid)

        assertThat(uuid).isEqualTo(activitiesById.id)
    }
}