package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.ui.dto.configuration.ConfigurationDto
import com.vocalink.crossproduct.ui.facade.api.ConfigurationFacade
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.Charset

@WebMvcTest(ConfigurationController::class)
@ContextConfiguration(classes = [TestConfig::class])
class ConfigurationControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var configurationFacade: ConfigurationFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(
        MediaType.APPLICATION_JSON.type,
        MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8")
    )

    private companion object {
        const val CONTEXT = "context"
        const val CLIENT_TYPE = "client-type"

        const val VALID_CONFIGURATION_RESPONSE = """{
            "scheme": "P27-SEK",
            "schemeCurrency": "SEK",
            "dataRetentionDays": 30
        }
        """
    }

    @Test
    fun `should return 200 on configuration request`() {
        val configurationDto = ConfigurationDto(
            "P27-SEK", "SEK", 30
        )

        Mockito.`when`(configurationFacade.getConfiguration(any(), any()))
            .thenReturn(configurationDto)

        mockMvc.perform(
            get("/configuration")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT, TestConstants.CONTEXT)
                .header(CLIENT_TYPE, TestConstants.CLIENT_TYPE)
        )
            .andExpect(status().isOk)
            .andExpect(content().json(VALID_CONFIGURATION_RESPONSE));
    }
}