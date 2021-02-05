package com.vocalink.crossproduct.ui.controllers.impl;

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.reference.ParticipantReference
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.broadcasts.BroadcastDto
import com.vocalink.crossproduct.ui.facade.BroadcastsFacade
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import java.time.ZonedDateTime

@WebMvcTest(BroadcastsController::class)
@ContextConfiguration(classes = [TestConfig::class])
class BroadcastsControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var facade: BroadcastsFacade

    companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val MIN_EXPECTED_RESPONSE = """
        {
        "totalResults": 0,
        "items": []
        }
        """
        const val EXPECTED_RESPONSE = """
        {
        "totalResults": 1,
        "items": [
            {
                "createdAt": "2021-01-25T00:00:00Z",
                "broadcastId": "123",
                "message": "msg",
                "recipients": [
                    {
                        "participantIdentifier": "123",
                        "name": "name",
                        "participantType": "DIRECT",
                        "connectingParticipantId": "id",
                        "schemeCode": "p27"
                    }
                ]
            }
        ]
        }
        """
    }

    @Test
    fun `should have success on get broadcasts`() {
        `when`(facade.getPaginated(any(), any(), any())).thenReturn(PageDto(0, emptyList<BroadcastDto>()))

        mockMvc.perform(get("/broadcasts")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(MIN_EXPECTED_RESPONSE, true))
    }

    @Test
    fun `should have success on get broadcasts with query params`() {
        val queryParams = LinkedMultiValueMap(mapOf(Pair("offset", listOf("0")),
                Pair("limit", listOf("10")),
                Pair("date_From", listOf("2021-01-25T00:00:00Z")),
                Pair("date_To", listOf("2021-01-26T00:00:00Z")),
                Pair("sort", listOf("any")),
                Pair("msg", listOf("any")),
                Pair("recipient", listOf("any"))
        ))
        val ref = ParticipantReference("123", "name", ParticipantType.DIRECT, "id", "p27")
        val broadcastDto = BroadcastDto(ZonedDateTime.parse("2021-01-25T00:00:00Z"), "123", "msg", listOf(ref))

        val response = PageDto(1, listOf(broadcastDto))

        `when`(facade.getPaginated(any(), any(), any())).thenReturn(response)

        mockMvc.perform(get("/broadcasts")
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .queryParams(queryParams))
                .andExpect(status().isOk)
                .andExpect(content().json(EXPECTED_RESPONSE, true))
    }
}
