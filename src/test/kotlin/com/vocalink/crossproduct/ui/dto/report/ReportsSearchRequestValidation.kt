package com.vocalink.crossproduct.ui.dto.report

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import javax.validation.Validation.buildDefaultValidatorFactory
import javax.validation.Validator

class ReportsSearchRequestValidation {
    private lateinit var validator: Validator
    private lateinit var request: ReportsSearchRequest

    companion object {
        const val OLDER_THEN_30_ERROR = "date_from can not be earlier than 30 days"
    }

    @BeforeEach
    fun `init`() {
        request = ReportsSearchRequest()
        validator = buildDefaultValidatorFactory().validator
    }

    @Test
    fun `should fail on longer then 30`() {
        val older = ZonedDateTime.now().minusDays(31).toString()
        request.setDate_from(older)

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(OLDER_THEN_30_ERROR)
    }

    @Test
    fun `should pass on 30`() {
        val older = ZonedDateTime.now().minusDays(29).toString()
        request.setDate_from(older)

        val result = ArrayList(validator.validate(request))

        assertThat(result).isEmpty()
    }
}