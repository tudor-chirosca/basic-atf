package com.vocalink.crossproduct.ui.controllers

import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

class ActuatorLogfileEndpointTest : ControllerTest() {

    @Test
    fun `should return non-empty log file`() {
         mockMvc.perform(
            MockMvcRequestBuilders.get("/actuator/logfile")
                .servletPath("/actuator/logfile")
                .contentType(UTF8_CONTENT_TYPE)
        ).andExpect(content().string(not(Matchers.equalTo(""))))
    }
}
