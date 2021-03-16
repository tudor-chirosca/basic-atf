package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants.CLIENT_TYPE
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.ui.controllers.api.ReportApi
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.report.ReportDto
import com.vocalink.crossproduct.ui.facade.api.ReportFacade
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.MediaType.APPLICATION_OCTET_STREAM
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@WebMvcTest(ReportApi::class)
@ContextConfiguration(classes = [TestConfig::class])
class ReportControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var reportFacade: ReportFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(
        APPLICATION_JSON.type, APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val MIN_EXPECTED_RESPONSE = """
        {
        "totalResults": 0,
        "items": []
        }
        """

        const val EXPECTED_RESPONSE = """
        {
        "totalResults": 1,
        "items": [
                 {
                     "reportId": "10000000006",
                     "reportType": "PRE-SETTLEMENT_ADVICE",
                     "createdAt": "2021-02-14T00:00:00Z",
                     "cycleId": "20201231002",
                     "participantIdentifier": "IBCASES1",
                     "participantName": "ICA Banken" 
                }
            ]
        }
        """
    }

    @Test
    fun `should have success on get reports`() {
        `when`(reportFacade.getPaginated(any(), any(), any()))
            .thenReturn(PageDto(0, emptyList<ReportDto>()))

        mockMvc.perform(get("/reports")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
            .andExpect(status().isOk)
            .andExpect(content().json(MIN_EXPECTED_RESPONSE, true))
    }

    @Test
    fun `should have success on get reports with query params`() {
        val queryParams = LinkedMultiValueMap(
            mapOf(
                Pair("offset", listOf("0")),
                Pair("limit", listOf("10")),
                Pair("sort", listOf("any"))
            )
        )

        val reportDto = ReportDto(
            "10000000006",
            "PRE-SETTLEMENT_ADVICE",
            ZonedDateTime.parse("2021-02-14T00:00:00Z"),
            "20201231002",
            "IBCASES1",
            "ICA Banken"
        )

        val response = PageDto(1, listOf(reportDto))

        `when`(reportFacade.getPaginated(any(), any(), any()))
            .thenReturn(response)

        mockMvc.perform(get("/reports")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .queryParams(queryParams))
            .andExpect(status().isOk)
            .andExpect(content().json(EXPECTED_RESPONSE, true))
    }

    @Test
    fun `should fail with 400 when dateFrom is earlier than DAYS_LIMIT from today`() {
        val queryParams = LinkedMultiValueMap(
            mapOf(
                Pair("offset", listOf("0")),
                Pair("limit", listOf("10")),
                Pair("date_from", listOf(
                        ZonedDateTime.now()
                            .minusDays((getDefault(DtoProperties.DAYS_LIMIT).toLong()) + 1)
                            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))))
        )
        mockMvc.perform(
            get("/reports")
            .contentType(UTF8_CONTENT_TYPE)
            .header(CONTEXT_HEADER, CONTEXT)
            .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .queryParams(queryParams))
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(CoreMatchers.containsString("date_from can not be earlier than DAYS_LIMIT")))
    }

    @Test
    fun `should return report data for id` () {
        val id = "10000000006"
        val stream = InputStreamResource(ByteArrayInputStream(byteArrayOf(125, 12)))
        `when`(reportFacade.getReport(any(), any(), any())).thenReturn(stream)
        mockMvc.perform(get("/reports/$id")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .header(HttpHeaders.ACCEPT, APPLICATION_OCTET_STREAM))
                .andExpect(status().isOk)
                .andExpect(content().bytes(byteArrayOf(125, 12)))
    }
}