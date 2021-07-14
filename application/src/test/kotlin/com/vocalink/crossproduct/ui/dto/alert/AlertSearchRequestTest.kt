package com.vocalink.crossproduct.ui.dto.alert

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties.DAYS_LIMIT
import com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT
import com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET
import java.lang.Integer.parseInt
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
class AlertSearchRequestTest {

    private lateinit var validator: Validator

    private lateinit var objectMapper: ObjectMapper

    private companion object {
        const val REQUEST_WITH_DEFAULT_VALUES = """{"offset":0,"limit":20}"""
        const val OLDER_THEN_DAYS_LIMIT_ERROR = "date_from can not be earlier than DAYS_LIMIT"
        val clock = TestConstants.FIXED_CLOCK!!
    }

    @BeforeAll
    fun init() {
        validator = Validation.buildDefaultValidatorFactory().validator
        objectMapper = ObjectMapper()
    }

    @Test
    fun `should ignore null optional fields when serialize AlertSearchRequest`() {
        val request = AlertSearchRequest()

        val result = objectMapper.writeValueAsString(request)

        JSONAssert.assertEquals(REQUEST_WITH_DEFAULT_VALUES, result, true)
    }

    @Test
    fun `should set default values`() {
        val request = AlertSearchRequest()

        assertThat(request.offset).isEqualTo(parseInt(getDefault(OFFSET)))
        assertThat(request.limit).isEqualTo(parseInt(getDefault(LIMIT)))
    }

    @Test
    fun `should fail on earlier then DAYS_LIMIT`() {
        val request = AlertSearchRequest()
        request.setDate_from(
            ZonedDateTime.now(ZoneId.of("UTC"))
                .minusDays((getDefault(DAYS_LIMIT).toLong()) + 1)
                .format(ISO_ZONED_DATE_TIME)
        )

        val result = ArrayList(validator.validate(request))

        assertThat(result).isNotEmpty
        assertThat(result.stream().map { e -> e.message }).contains(OLDER_THEN_DAYS_LIMIT_ERROR)
    }

    @Test
    fun `should set fields with snake notation compatible setters`() {
        val dateFrom = ZonedDateTime.now(clock).minusMonths(1)
        val dateTo = ZonedDateTime.now(clock)
        val alertId = "alert_id"
        val request = AlertSearchRequest()

        request.setDate_from(dateFrom.format(ISO_ZONED_DATE_TIME))
        request.setDate_to(dateTo.format(ISO_ZONED_DATE_TIME))
        request.setAlert_id(alertId)

        assertThat(request.dateFrom).isEqualTo(dateFrom)
        assertThat(request.dateTo).isEqualTo(dateTo)
        assertThat(request.alertId).isEqualTo(alertId)
    }
}
