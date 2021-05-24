package com.vocalink.crossproduct.infrastructure.jpa.audit

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Sort.Order


class AuditDetailsJpaTest {

    @Test
    fun `should map sort parameters to JPA entity fields in different orders`() {
        val sortParameters = listOf("serviceId", "user", "-eventType")

        val result = AuditDetailsJpa.getSortBy(sortParameters)

        assertThat(result.toList()).containsExactly(
            Order.asc("serviceId"),
            Order.asc("first_name"),
            Order.asc("last_name"),
            Order.desc("userActivityString")
        )
    }

    @Test
    fun `should map sort parameters to JPA entity fields in descending order`() {
        val sortParameters = listOf("serviceId", "-user")

        val result = AuditDetailsJpa.getSortBy(sortParameters)

        assertThat(result.toList()).containsExactly(
            Order.asc("serviceId"),
            Order.desc("first_name"),
            Order.desc("last_name")
        )
    }

    @Test
    fun `should skip not existing fields when map sort parameters to JPA entity fields`() {
        val sortParameters = listOf("-serviceId", "not_exists")

        val result = AuditDetailsJpa.getSortBy(sortParameters)

        assertThat(result.toList()).containsExactly(Order.desc("serviceId"))
        assertThat(result.toList()).doesNotContain(Order.asc("not_exists"))
    }

    @Test
    fun `should return default value when sort parameters are empty`() {
        val sortParameters = ArrayList<String>()

        val result = AuditDetailsJpa.getSortBy(sortParameters)

        assertThat(result.toList()).isNotEmpty
    }

    @Test
    fun `should return default value when sort parameters are null`() {
        val result = AuditDetailsJpa.getSortBy(null)

        assertThat(result.toList()).isNotEmpty
    }
}