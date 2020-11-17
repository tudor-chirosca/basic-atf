package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants.CLIENT_TYPE
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto
import com.vocalink.crossproduct.ui.facade.AlertsServiceFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.Charset

@WebMvcTest(AlertsController::class)
class AlertsControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var facade: AlertsServiceFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val VALID_REQUEST = "{}"
        const val VALID_PARTIAL_ALERT_REQUEST: String = """ 
        {
          "offset": 0,
          "limit": 20,
          "priorities": [ "high"
          ],
          "dateFrom": "2020-12-23",
          "dateTo": "2020-12-28",
          "alertTypes": [
          ],
          "entities": [
          ],
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
        val alertReferenceDataDto = AlertReferenceDataDto.builder().build()
        `when`(facade.getAlertsReference(CONTEXT, ClientType.UI))
                .thenReturn(alertReferenceDataDto)
        mockMvc.perform(get("/reference/alerts")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)

        verify(facade).getAlertsReference(any(), any())
    }

    @Test
    @Throws(Exception::class)
    fun `should get Alert Stats`() {
        val alertStatsDto = AlertStatsDto.builder().build()

        `when`(facade.getAlertStats(CONTEXT, ClientType.UI))
                .thenReturn(alertStatsDto)
        mockMvc.perform(get("/alerts/stats")
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().isOk)

        verify(facade).getAlertStats(any(), any())
    }

    @Test
    fun `should return 200 if no criteria specified in request`() {
        `when`(facade.getAlerts(any(), any(), any())).thenReturn(PageDto(0, null))

        mockMvc.perform(post("/alerts")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .content(VALID_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_RESPONSE))
    }

    @Test
    fun `should return 200 if some criteria specified in request`() {
        mockMvc.perform(post("/alerts")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .content(VALID_PARTIAL_ALERT_REQUEST))
                .andExpect(status().isOk)
    }

    @Test
    fun `should fail with 400 when request body missing`() {
        mockMvc.perform(post("/alerts")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE))
                .andExpect(status().is4xxClientError)
    }
}
