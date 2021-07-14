package com.vocalink.crossproduct.ui.dto.settlement

import com.fasterxml.jackson.databind.ObjectMapper
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration.getDefault
import com.vocalink.crossproduct.ui.dto.DtoProperties.LIMIT
import com.vocalink.crossproduct.ui.dto.DtoProperties.OFFSET
import java.lang.Integer.parseInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.skyscreamer.jsonassert.JSONAssert

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParticipantSettlementRequestTest {

    private lateinit var objectMapper: ObjectMapper

    private companion object {
        const val REQUEST_WITH_DEFAULT_VALUES = """{"offset":0,"limit":20}"""
    }

    @BeforeAll
    fun init() {
        objectMapper = ObjectMapper()
    }

    @Test
    fun `should ignore null optional fields when serialize ParticipantSettlementRequest`() {
        val request = ParticipantSettlementRequest()

        val result = objectMapper.writeValueAsString(request)

        JSONAssert.assertEquals(REQUEST_WITH_DEFAULT_VALUES, result, true)
    }

    @Test
    fun `should set default values`() {
        val request = ParticipantSettlementRequest()

        assertThat(request.offset).isEqualTo(parseInt(getDefault(OFFSET)))
        assertThat(request.limit).isEqualTo(parseInt(getDefault(LIMIT)))
    }

}
