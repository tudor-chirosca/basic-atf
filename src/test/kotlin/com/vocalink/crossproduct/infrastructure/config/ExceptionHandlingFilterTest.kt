package com.vocalink.crossproduct.infrastructure.config

import com.vocalink.crossproduct.ui.controllers.SettlementDashboardController
import com.vocalink.crossproduct.ui.exceptions.GlobalExceptionHandler
import com.vocalink.crossproduct.ui.facade.SettlementDashboardFacade
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders


@WebMvcTest(value = [SettlementDashboardController::class])
class ExceptionHandlingFilterTest @Autowired constructor(var mockMvc: MockMvc) {

    @MockBean
    private lateinit var dashboard: SettlementDashboardFacade

    @MockBean
    private lateinit var handler: GlobalExceptionHandler

    @Test
    fun `verify global exception handler invoked`() {
        mockMvc.perform(MockMvcRequestBuilders.get("/settlement"))

        verify(handler, atLeastOnce()).handleException(any(), any())
    }
}
