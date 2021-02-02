package com.vocalink.crossproduct.ui.controllers.impl;

import com.vocalink.crossproduct.TestConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@WebMvcTest(BroadcastsController::class)
@ContextConfiguration(classes=[TestConfig::class])
class BroadcastsControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @Test
    fun `should get broadcasts`() {
        mockMvc.perform(get("/broadcasts"))
    }

}