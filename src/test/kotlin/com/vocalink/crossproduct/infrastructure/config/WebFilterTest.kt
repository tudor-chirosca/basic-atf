package com.vocalink.crossproduct.infrastructure.config

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.ui.controllers.SettlementDashboardController
import com.vocalink.crossproduct.ui.facade.api.SettlementDashboardFacade
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.atMostOnce
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(value = [SettlementDashboardController::class])
@ContextConfiguration(classes=[TestConfig::class])
class WebFilterTest @Autowired constructor(var mockMvc: MockMvc) {

    @MockBean
    private lateinit var dashboard: SettlementDashboardFacade

    @Test
    fun `should reach service on success`() {
        mockMvc.perform(get("/settlement")
                .header("context", "BPS")
                .header("client-type", "UI"))
                .andExpect(status().isOk)

        verify(dashboard, atMostOnce()).getParticipantSettlement(anyString(), any(), anyString())
    }

    @Test
    fun `context filter should throw exception if missing header`() {
        mockMvc.perform(get("/settlement")
                .header("client-type", "UI"))
                .andExpect(status().is5xxServerError)
                .andExpect(jsonPath("$.Errors.Error[0].Description")
                        .value("Missing or invalid request header 'context': null"))
    }

    @Test
    fun `client-type filter should throw exception if missing header`() {
        mockMvc.perform(get("/settlement")
                .header("context", "BPS"))
                .andExpect(status().is5xxServerError)
                .andExpect(jsonPath("$.Errors.Error[0].Description")
                        .value("Missing or invalid request header 'client-type': null"))
    }
}
