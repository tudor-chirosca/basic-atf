package com.vocalink.crossproduct.ui.controllers;

import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ActuatorInfoEndpointTest : ControllerTest() {

    @Test
    fun `should return response containing pom version and bd details`() {
        mockMvc.perform(get("/actuator/info")
                .servletPath("/actuator/info")
                .contentType(UTF8_CONTENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.issVersion").exists())
                .andExpect(jsonPath("$.migration").exists())
                .andExpect(jsonPath("$.migration.releaseDate").exists())
                .andExpect(jsonPath("$.migration.version").exists())
    }
}
