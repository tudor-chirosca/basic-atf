package com.vocalink.crossproduct.ui.dto.report

import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties
import java.time.ZoneId
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
        const val OLDER_THEN_DAYS_LIMIT_ERROR = "date_from can not be earlier than DAYS_LIMIT"
    }

    @BeforeEach
    fun `init`() {
        request = ReportsSearchRequest()
        validator = buildDefaultValidatorFactory().validator
    }

    @Test
    fun `should fail on longer then DAYS_LIMIT`() {
        val older = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(
            (getDefault(DtoProperties.DAYS_LIMIT).toLong())+1).toString()
        request.setDate_from(older)

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result[0].message).isEqualTo(OLDER_THEN_DAYS_LIMIT_ERROR)
    }

    @Test
    fun `should pass on less then DAYS_LIMIT`() {
        val older = ZonedDateTime.now(ZoneId.of("UTC")).minusDays(
            (getDefault(DtoProperties.DAYS_LIMIT).toLong())-1).toString()
        request.setDate_from(older)

        val result = ArrayList(validator.validate(request))

        assertThat(result).isEmpty()
    }
}