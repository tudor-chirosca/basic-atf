package com.vocalink.crossproduct.ui.dto.batch

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT
import com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET
import java.lang.Integer.parseInt
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert

class BatchEnquirySearchRequestTest {

    private lateinit var request: BatchEnquirySearchRequest

    private lateinit var objectMapper: ObjectMapper

    companion object {
        val clock = TestConstants.FIXED_CLOCK!!
        const val REQUEST_WITH_DEFAULT_VALUES = """{"offset":0,"limit":20}"""
    }

    @BeforeEach
    fun `init`() {
        request = BatchEnquirySearchRequest()
        objectMapper = ObjectMapper()
    }

    @Test
    fun `should set fields with snake notation compatible setters`() {
        val dateFrom = ZonedDateTime.now(clock).minusMonths(1)
        val dateTo = ZonedDateTime.now(clock)
        val cycleId = "cycle_id"
        val messageDirection = "message_direction"
        val messageType = "message_type"

        request.setDate_from(dateFrom.format(ISO_ZONED_DATE_TIME))
        request.setDate_to(dateTo.format(ISO_ZONED_DATE_TIME))
        request.setCycle_id(cycleId)
        request.setMsg_direction(messageDirection)
        request.setMsg_type(messageType)

        assertThat(request.dateFrom).isEqualTo(dateFrom)
        assertThat(request.dateTo).isEqualTo(dateTo)
        assertThat(request.cycleId).isEqualTo(cycleId)
        assertThat(request.messageDirection).isEqualTo(messageDirection)
        assertThat(request.messageType).isEqualTo(messageType)
    }

    @Test
    fun `should set default values`() {
        assertThat(request.offset).isEqualTo(parseInt(getDefault(OFFSET)))
        assertThat(request.limit).isEqualTo(parseInt(getDefault(LIMIT)))
    }

    @Test
    fun `should ignore null optional fields when serialize BatchEnquirySearchRequest`() {
        val result = objectMapper.writeValueAsString(request)

        JSONAssert.assertEquals(REQUEST_WITH_DEFAULT_VALUES, result, true)
    }

}
