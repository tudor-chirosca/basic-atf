package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants.CLIENT_TYPE
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest
import com.vocalink.crossproduct.ui.facade.api.ParticipantFacade
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
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

@WebMvcTest(AlertsController::class)
@ContextConfiguration(classes = [TestConfig::class])
class ParticipantsControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var facade: ParticipantFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val VALID_REQUEST = "{}"
        const val VALID_PARTIAL_PARTICIPANT_REQUEST: String = """
        {
            "offset": 0,
            "limit": 20,
            "sort": "name"
        }"""

        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_RESPONSE = """
        {
            "totalResults": 22,
            "items": [
            {
                "bic": "FORXSES1",
                "fundingBic": null,
                "id": "FORXSES1",
                "name": "Forex Bank",
                "status": "ACTIVE",
                "suspendedTime": null,
                "participantType": "FUNDED",
                "organizationId": "194869924",
                "hasActiveSuspensionRequests": false,
                "tpspName": "Nordnet Bank",
                "tpspId": "475347837892",
                "fundedParticipantsCount": 0
            }]
        }"""
    }

    val items = ManagedParticipantDto("FORXSES1", null, "FORXSES1", "Forex Bank", ParticipantStatus.ACTIVE,
            null, ParticipantType.FUNDED, "194869924", "Nordnet Bank", "475347837892",
            null, 0)

    @Test
    fun `should return 200 if no criteria specified and return valid response`() {
        `when`(facade.getPaginated(any(), any(), any(ManagedParticipantsSearchRequest::class.java)))
                .thenReturn(PageDto(22, listOf(items)))

        mockMvc.perform(get("/participants")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .content(VALID_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_RESPONSE, true))
    }

    @Test
    fun `should return 200 if some criteria specified in request`() {
        `when`(facade.getPaginated(any(), any(), any(ManagedParticipantsSearchRequest::class.java)))
                .thenReturn(PageDto(22, listOf(items)))


        mockMvc.perform(get("/participants")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .content(VALID_PARTIAL_PARTICIPANT_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_RESPONSE, true))
    }
}