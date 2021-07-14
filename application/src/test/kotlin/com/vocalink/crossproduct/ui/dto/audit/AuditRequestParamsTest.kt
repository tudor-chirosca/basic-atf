package com.vocalink.crossproduct.ui.dto.audit

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import javax.validation.Validation
import javax.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.skyscreamer.jsonassert.JSONAssert

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuditRequestParamsTest {

    private lateinit var validator: Validator

    private lateinit var objectMapper: ObjectMapper

    private companion object {
        const val REQUEST_WITH_DEFAULT_VALUES = """{"offset":0,"limit":0}"""
        const val LIMIT_LESS_THAN_ONE = "Limit should be equal or higher than 1"
        const val OLDER_THEN_DAYS_LIMIT_ERROR = "date_from can not be earlier than DAYS_LIMIT"
        val clock = TestConstants.FIXED_CLOCK!!
    }

    @BeforeAll
    fun init() {
        validator = Validation.buildDefaultValidatorFactory().validator
        objectMapper = ObjectMapper()
    }

    @Test
    fun `should ignore null optional fields when serialize AuditRequestParams`() {
        val request = AuditRequestParams()

        val result = objectMapper.writeValueAsString(request)

        JSONAssert.assertEquals(REQUEST_WITH_DEFAULT_VALUES, result, true)
    }

    @Test
    fun `should set fields with snake notation compatible setters`() {
        val dateFrom = ZonedDateTime.now(clock).minusMonths(1)
        val dateTo = ZonedDateTime.now(clock)
        val eventTypes = listOf("event_type", "event_type1")
        val request = AuditRequestParams()

        request.setDate_from(dateFrom.format(ISO_ZONED_DATE_TIME))
        request.setDate_to(dateTo.format(ISO_ZONED_DATE_TIME))
        request.setEvent_types(eventTypes)

        assertThat(request.dateFrom).isEqualTo(dateFrom)
        assertThat(request.dateTo).isEqualTo(dateTo)
        assertThat(request.events).containsExactlyElementsOf(eventTypes)
    }

    @Test
    fun `should fail if limit less then 1`() {
        val request = AuditRequestParams()
        request.limit = 0

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result.stream().map { e -> e.message }).contains(LIMIT_LESS_THAN_ONE)
    }

    @Test
    fun `should fail on earlier then DAYS_LIMIT`() {
        val request = AuditRequestParams()
        request.setDate_from(
            ZonedDateTime.now(ZoneId.of("UTC"))
                .minusDays((getDefault(DAYS_LIMIT).toLong()) + 1)
                .format(ISO_ZONED_DATE_TIME)
        )

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result.stream().map { e -> e.message }).contains(OLDER_THEN_DAYS_LIMIT_ERROR)
    }
}
