package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordDto
import com.vocalink.crossproduct.ui.dto.routing.RoutingRecordRequest
import com.vocalink.crossproduct.ui.facade.api.RoutingRecordFacade
import java.nio.charset.Charset
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(RoutingRecordsController::class)
@ContextConfiguration(classes = [TestConfig::class])
class RoutingRecordsControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var facade: RoutingRecordFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val VALID_REQUEST = "{}"
        const val VALID_PARTIAL_PARTICIPANT_REQUEST: String = """
        {
            "offset": 0,
            "limit": 20,
            "sort": "name"
        }"""

        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_ROUTING_RECORD_RESPONSE = """
        {
            "totalResults": 1,
            "items": [
                {
                    "reachableBic": "NDEASESSXXY",
                    "validFrom": "2021-01-24T00:00:00Z",
                    "validTo": "2021-02-03T00:00:00Z",
                    "currency": "SEK"
                }
            ]
        }
        """
    }

    @Test
    fun `should return 200 on routing records on empty request`() {
        val routingRecords = RoutingRecordDto(
                "NDEASESSXXY",
                ZonedDateTime.of(2021, Month.JANUARY.value, 24, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2021, Month.FEBRUARY.value, 3, 0, 0, 0, 0, ZoneId.of("UTC")),
                "SEK"
        )
        `when`(facade.getPaginated(any(), any(), any(RoutingRecordRequest::class.java), any()))
                .thenReturn(PageDto(1, listOf(routingRecords)))

        mockMvc.perform(get("/participants/NDEASESSXXY/routing")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_ROUTING_RECORD_RESPONSE, true))
    }

    @Test
    fun `should return 200 on routing records on request`() {
        val routingRecords = RoutingRecordDto(
                "NDEASESSXXY",
                ZonedDateTime.of(2021, Month.JANUARY.value, 24, 0, 0, 0, 0, ZoneId.of("UTC")),
                ZonedDateTime.of(2021, Month.FEBRUARY.value, 3, 0, 0, 0, 0, ZoneId.of("UTC")),
                "SEK"
        )
        `when`(facade.getPaginated(any(), any(), any(RoutingRecordRequest::class.java), any()))
                .thenReturn(PageDto(1, listOf(routingRecords)))

        mockMvc.perform(get("/participants/NDEASESSXXY/routing")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .content(VALID_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_ROUTING_RECORD_RESPONSE, true))
    }
}