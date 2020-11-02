package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.alert.AlertReferenceDataDto
import com.vocalink.crossproduct.ui.facade.AlertsServiceFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest(AlertsController::class)
class AlertsControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var facade: AlertsServiceFacade

    @Test
    @Throws(Exception::class)
    fun `should get bad request on missing context for alert reference`() {
        val alertReferenceDataDto = AlertReferenceDataDto.builder().build()

        Mockito.`when`(facade.getAlertsReference(TestConstants.CONTEXT, ClientType.UI))
                .thenReturn(alertReferenceDataDto)
        mockMvc.perform(get("/reference/alerts")
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(status().isBadRequest)
    }

    @Test
    @Throws(Exception::class)
    fun `should get IO Details`() {
        val alertReferenceDataDto = AlertReferenceDataDto.builder().build()
        Mockito.`when`(facade.getAlertsReference(TestConstants.CONTEXT, ClientType.UI))
                .thenReturn(alertReferenceDataDto)
        mockMvc.perform(get("/reference/alerts")
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)

        Mockito.verify(facade).getAlertsReference(any(), any())
    }
}