package com.vocalink.crossproduct.ui.dto.broadcast

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BroadcastRequestTest {

    private lateinit var objectMapper: ObjectMapper

    private companion object {
        const val VALID_TRUE_RESPONSE =
                """{"message":"message","recipients":[],"broadcastForAll":true}"""

        const val VALID_FALSE_RESPONSE_WITH_IDS =
                """{"message":"message","recipients":["NDEASESSXXX"],"broadcastForAll":false}"""

        const val VALID_FALSE_RESPONSE_WITH_NULL =
                """{"message":"message","recipients":null,"broadcastForAll":false}"""
    }

    @BeforeAll
    fun init() {
        objectMapper = ObjectMapper()
    }

    @Test
    fun `should get true for broadcastForAll field with empty recipients list`() {
        val request = BroadcastRequest("message", emptyList())

        val result = objectMapper.writeValueAsString(request)

        assertThat(result).isEqualTo(VALID_TRUE_RESPONSE)
    }

    @Test
    fun `should get false for broadcastForAll field with specified recipients`() {
        val request = BroadcastRequest("message", listOf("NDEASESSXXX"))

        val result = objectMapper.writeValueAsString(request)

        assertThat(result).isEqualTo(VALID_FALSE_RESPONSE_WITH_IDS)
    }

    @Test
    fun `should get false for broadcastForAll field with null value for recipients`() {
        val request = BroadcastRequest("message", null)

        val result = objectMapper.writeValueAsString(request)

        assertThat(result).isEqualTo(VALID_FALSE_RESPONSE_WITH_NULL)
    }

}
