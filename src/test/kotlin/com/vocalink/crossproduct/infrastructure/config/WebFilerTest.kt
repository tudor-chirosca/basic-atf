package com.vocalink.crossproduct.infrastructure.config

import com.vocalink.crossproduct.ui.controllers.SettlementController
import com.vocalink.crossproduct.ui.facade.SettlementServiceFacade
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.atMostOnce
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(value = [SettlementController::class])
class WebFilerTest @Autowired constructor(var mockMvc: MockMvc) {

    @MockBean
    private lateinit var service: SettlementServiceFacade

    @Test
    fun `should reach service on success`() {
        mockMvc.perform(get("/settlement")
                .header("context", "BPS")
                .header("client-type", "UI"))
                .andExpect(status().isOk)

        verify(service, atMostOnce()).getSettlement(anyString(), any())
    }

    @Test
    fun `context filter should throw exception if missing header`() {
        mockMvc.perform(get("/settlement")
                .header("client-type", "UI"))
                .andExpect(status().is5xxServerError)
                .andExpect(jsonPath("$.message")
                        .value("Missing or invalid request header 'context': null"))
    }

    @Test
    fun `client-type filter should throw exception if missing header`() {
        mockMvc.perform(get("/settlement")
                .header("context", "BPS"))
                .andExpect(status().is5xxServerError)
                .andExpect(jsonPath("$.message")
                        .value("Missing or invalid request header 'client-type': null"))
    }
}
