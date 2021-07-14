package com.vocalink.crossproduct.ui.dto.report

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT
import com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET
import java.lang.Integer.parseInt
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.skyscreamer.jsonassert.JSONAssert

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportsSearchRequestTest {

    private lateinit var objectMapper: ObjectMapper

    private companion object {
        const val REQUEST_WITH_DEFAULT_VALUES = """{"offset":0,"limit":20}"""
        val clock = TestConstants.FIXED_CLOCK!!
    }

    @BeforeAll
    fun init() {
        objectMapper = ObjectMapper()
    }

    @Test
    fun `should ignore null optional fields when serialize ReportsSearchRequest`() {
        val request = ReportsSearchRequest()

        val result = objectMapper.writeValueAsString(request)

        JSONAssert.assertEquals(REQUEST_WITH_DEFAULT_VALUES, result, true)
    }

    @Test
    fun `should set fields with snake notation compatible setters`() {
        val dateFrom = ZonedDateTime.now(clock).minusMonths(1)
        val dateTo = ZonedDateTime.now(clock)
        val reportTypes = listOf("report_type", "report_type1")
        val request = ReportsSearchRequest()

        request.setDate_from(dateFrom.format(ISO_ZONED_DATE_TIME))
        request.setDate_to(dateTo.format(ISO_ZONED_DATE_TIME))
        request.setReport_types(reportTypes)

        assertThat(request.dateFrom).isEqualTo(dateFrom)
        assertThat(request.dateTo).isEqualTo(dateTo)
        assertThat(request.reportTypes).containsExactlyElementsOf(reportTypes)
    }

    @Test
    fun `should set default values`() {
        val request = ReportsSearchRequest()

        assertThat(request.offset).isEqualTo(parseInt(getDefault(OFFSET)))
        assertThat(request.limit).isEqualTo(parseInt(getDefault(LIMIT)))
    }

}
