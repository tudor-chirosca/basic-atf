package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto
import com.vocalink.crossproduct.ui.dto.alert.AlertStatsDto
import com.vocalink.crossproduct.ui.dto.alert.AlertDataDto
import com.vocalink.crossproduct.ui.dto.alert.AlertFilterRequest
import com.vocalink.crossproduct.ui.facade.AlertsServiceFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.Charset

@WebMvcTest(AlertsController::class)
class AlertsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var facade: AlertsServiceFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    companion object {
        const val VALID_ALERT_REQUEST: String = """ 
        {
            "offset": 0,
            "limit": 20,
            "sort": "dateRaised",
            "order": "DESC"
        }"""
    }

    @Test
    @Throws(Exception::class)
    fun `should get Alert References`() {
        val alertReferenceDataDto = AlertReferenceDataDto.builder().build()
        Mockito.`when`(facade.getAlertsReference(TestConstants.CONTEXT, ClientType.UI))
                .thenReturn(alertReferenceDataDto)
        mockMvc.perform(get("/reference/alerts")
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)

        Mockito.verify(facade).getAlertsReference(any(), any())
    }

    @Test
    @Throws(Exception::class)
    fun `should get Alert Stats`() {
        val alertStatsDto = AlertStatsDto.builder().build()

        Mockito.`when`(facade.getAlertStats(TestConstants.CONTEXT, ClientType.UI))
                .thenReturn(alertStatsDto)
        mockMvc.perform(get("/alerts/stats")
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)

        Mockito.verify(facade).getAlertStats(any(), any())
    }

    @Test
    fun `should get Alerts`() {
        val alertDataDto = AlertDataDto()
        val alertFilterRequest = AlertFilterRequest()

        Mockito.`when`(facade.getAlerts(TestConstants.CONTEXT, ClientType.UI, alertFilterRequest))
                .thenReturn(alertDataDto)

        mockMvc.perform(post("/alerts")
                .contentType(UTF8_CONTENT_TYPE)
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE)
                .content(VALID_ALERT_REQUEST))
                .andExpect(status().isOk)

        Mockito.verify(facade).getAlerts(any(), any(), any())
    }
}
