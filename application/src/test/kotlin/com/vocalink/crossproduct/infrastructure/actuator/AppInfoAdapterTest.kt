package com.vocalink.crossproduct.infrastructure.actuator;

import com.vocalink.crossproduct.infrastructure.jpa.info.DatabaseVersionRepositoryJpa
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock


class AppInfoAdapterTest {

    private var mockRepository = mock(DatabaseVersionRepositoryJpa::class.java)

    private var appInfoAdapter = AppInfoAdapter(mockRepository)

    private val NOT_AVAILABLE = "n/a"

    @Test
    fun `should return not available when repository did not return values`() {
        val migrationVersion = appInfoAdapter.latestMigrationVersion

        assertThat(NOT_AVAILABLE).isEqualTo(migrationVersion["version"])
        assertThat(NOT_AVAILABLE).isEqualTo(migrationVersion["releaseDate"])
    }

    @Test
    fun `should return not available when no value inserted in pom version`() {
        assertThat(NOT_AVAILABLE).isEqualTo(appInfoAdapter.pomVersion)
    }
}
