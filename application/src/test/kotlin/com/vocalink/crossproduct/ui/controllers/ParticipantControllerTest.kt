package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants.CLIENT_TYPE
import com.vocalink.crossproduct.TestConstants.CONTEXT
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.participant.SuspensionLevel
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.participant.ApprovalReferenceDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantDto
import com.vocalink.crossproduct.ui.dto.participant.ManagedParticipantsSearchRequest
import com.vocalink.crossproduct.ui.facade.api.ParticipantFacade
import java.nio.charset.Charset
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

@WebMvcTest(ParticipantController::class)
@ContextConfiguration(classes = [TestConfig::class])
class ParticipantControllerTest constructor(@Autowired var mockMvc: MockMvc) {

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

        const val VALID_MANAGED_PARTICIPANTS_RESPONSE = """
        {
            "totalResults": 22,
            "items": [
            {
                "bic": "FORXSES1",
                "fundingBic": null,
                "id": "FORXSES1",
                "name": "Forex Bank",
                "status": "ACTIVE",
                "participantType": "FUNDED",
                "organizationId": "194869924",
                "hasActiveSuspensionRequests": false,
                "tpspName": "Nordnet Bank",
                "tpspId": "475347837892",
                "fundedParticipantsCount": 0,
                "approvalReference": {},
                "suspensionLevel": null
            },
            {
                "bic": "SWEDENBB",
                "fundingBic": null,
                "id": "SWEDENBB",
                "name": "SWEDBB Bank",
                "status": "SUSPENDED",
                "participantType": "DIRECT",
                "organizationId": "194869753",
                "hasActiveSuspensionRequests": false,
                "tpspName": "SWEDBB Bank",
                "tpspId": "115379817890",
                "fundedParticipantsCount": 0,
                "approvalReference": {},
                "suspensionLevel": "SCHEME_FUNDING"
            }]
        }"""
    }

    val items = listOf(
        ManagedParticipantDto.builder()
            .bic("FORXSES1")
            .id("FORXSES1")
            .name("Forex Bank")
            .status(ParticipantStatus.ACTIVE)
            .participantType(ParticipantType.FUNDED)
            .organizationId("194869924")
            .tpspId("475347837892")
            .tpspName("Nordnet Bank")
            .approvalReference(ApprovalReferenceDto.builder().build())
            .build(),
        ManagedParticipantDto.builder()
            .bic("SWEDENBB")
            .id("SWEDENBB")
            .name("SWEDBB Bank")
            .status(ParticipantStatus.SUSPENDED)
            .participantType(ParticipantType.DIRECT)
            .organizationId("194869753")
            .tpspId("115379817890")
            .tpspName("SWEDBB Bank")
            .approvalReference(ApprovalReferenceDto.builder().build())
            .suspensionLevel(SuspensionLevel.SCHEME_FUNDING)
            .build())

    @Test
    fun `should return 200 if no criteria specified and return valid response`() {
        `when`(facade.getPaginated(any(), any(), any(ManagedParticipantsSearchRequest::class.java), any()))
                .thenReturn(PageDto(22, items))

        mockMvc.perform(get("/participants")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .content(VALID_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_MANAGED_PARTICIPANTS_RESPONSE, true))
    }

    @Test
    fun `should return 200 if some criteria specified in request`() {
        `when`(facade.getPaginated(any(), any(), any(ManagedParticipantsSearchRequest::class.java), any()))
                .thenReturn(PageDto(22, items))

        mockMvc.perform(get("/participants")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, CONTEXT)
                .header(CLIENT_TYPE_HEADER, CLIENT_TYPE)
                .content(VALID_PARTIAL_PARTICIPANT_REQUEST))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_MANAGED_PARTICIPANTS_RESPONSE, true))
    }
}