package com.vocalink.crossproduct.infrastructure.config

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.ui.controllers.ReportController
import com.vocalink.crossproduct.ui.facade.api.ReportFacade
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(value = [ReportController::class])
@ContextConfiguration(classes = [TestConfig::class])
class RateLimiterFilterTest @Autowired constructor(var mockMvc: MockMvc) {

    @MockBean
    private lateinit var reportFacade: ReportFacade

    @Test
    fun `should return 429 and throw exception when on report download`() {
        val path = "/reports/10000000001"

        mockMvc.perform(get(path)
                .servletPath(path)
                .header("context", "BPS")
                .header("client-type", "UI")
                .header("x-user-id", "12a511"))
                .andExpect(MockMvcResultMatchers.status().isOk)

        mockMvc.perform(get(path)
                .servletPath(path)
                .header("context", "BPS")
                .header("client-type", "UI")
                .header("x-user-id", "12a511"))
                .andExpect(MockMvcResultMatchers.status().isTooManyRequests)
    }
}
