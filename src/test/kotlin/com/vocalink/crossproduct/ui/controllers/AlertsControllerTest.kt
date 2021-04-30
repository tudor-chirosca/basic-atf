package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants.CLIENT_TYPE
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto
import com.vocalink.crossproduct.ui.dto.alert.AlertSearchRequest
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto
import com.vocalink.crossproduct.ui.facade.api.AlertsServiceFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AlertsControllerTest : ControllerTest() {

    @MockBean
    private lateinit var facade: AlertsServiceFacade

    private companion object {
        const val VALID_EMPTY_REQUEST = "{}"
        const val VALID_PARTIAL_ALERT_REQUEST: String = """
        {
          "offset": 0,
          "limit": 20,
          "priorities": [ "high" ],
          "dateFrom": "2020-10-23T10:39:39Z",
          "dateTo": "2020-10-28T10:39:39Z",
          "alertTypes": [],
          "entities": [],
          "alertId": "142"
        }"""

        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_RESPONSE = """{
            "totalResults": 0,
            "items": []
        }"""
    }

    @Test
    @Throws(Exception::class)
    fun `should get Alert References`() {
        val alertReferenceDataDto = AlertReferenceDataDto(null, null)
        `when`(facade.getAlertsReference(CONTEXT, ClientType.UI))
                .thenReturn(alertReferenceDataDto)
        mockMvc.perform(get("/reference/alerts")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)

        verify(facade).getAlertsReference(any(), any())
        verifyNoInteractions(auditFacade)
    }

    @Test
    @Throws(Exception::class)
    fun `should get Alert Stats`() {
        val alertStatsDto = AlertStatsDto(0, null)

        `when`(facade.getAlertStats(CONTEXT, ClientType.UI))
                .thenReturn(alertStatsDto)
        mockMvc.perform(get("/alerts/stats")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)

        verify(facade).getAlertStats(any(), any())
        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should return 200 if no criteria specified in request`() {
        `when`(facade.getAlerts(any(), any(), any(AlertSearchRequest::class.java))).thenReturn(PageDto(0, null))

        mockMvc.perform(get("/alerts")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .content(VALID_EMPTY_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_RESPONSE, true))

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.VIEW_ALERTS)
    }

    @Test
    fun `should return 200 if some criteria specified in request`() {
        mockMvc.perform(get("/alerts")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .content(VALID_PARTIAL_ALERT_REQUEST))
                .andExpect(status().isOk)

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.VIEW_ALERTS)
    }

    @Test
    fun `should fail with 400 when dateFrom is earlier than DAYS_LIMIT from today`() {
        val queryParams = LinkedMultiValueMap(
            mapOf(
                Pair("offset", listOf("0")),
                Pair("limit", listOf("10")),
                Pair("date_from", listOf(
                    ZonedDateTime.now(ZoneId.of("UTC"))
                        .minusDays((getDefault(DtoProperties.DAYS_LIMIT).toLong()) + 1)
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))))
        )
        mockMvc.perform(
                get("/alerts")
                        .contentType(UTF8_CONTENT_TYPE)
                        .header(CONTEXT_HEADER, CONTEXT)
                        .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                        .queryParams(queryParams))
                .andExpect(status().is4xxClientError)
                .andExpect(content().string(containsString("date_from can not be earlier than DAYS_LIMIT")))
        verifyNoInteractions(auditFacade)
    }

}
