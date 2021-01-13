package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.shared.settlement.SettlementStatus
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantInstructionDto
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto
import com.vocalink.crossproduct.ui.facade.SettlementsFacade
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
import java.math.BigDecimal
import java.nio.charset.Charset
import java.time.ZoneId
import java.time.ZonedDateTime

@WebMvcTest(SettlementsController::class)
@ContextConfiguration(classes=[TestConfig::class])
class SettlementsControllerTest constructor(@Autowired var mockMvc: MockMvc) {

    @MockBean
    private lateinit var settlementsFacade: SettlementsFacade

    private val UTF8_CONTENT_TYPE: MediaType = MediaType(MediaType.APPLICATION_JSON.type,
            MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_DETAILS_RESPONSE = """{
            "cycleId": "20201209001",
            "settlementTime": "2020-12-09T15:58:19Z",
            "status": "PARTIAL",
            "participant": {
                "participantIdentifier": "HANDSESS",
                "name": "Svenska Handelsbanken",
                "participantType": "FUNDED",
                "connectingParticipantId": "NDEASESSXXX"
            },
            "instructions": {
                "totalResults": 1,
                "items": [
        	    {
                    "reference": "538264950",
                    "status": "PARTIAL",
                    "counterparty": {
                        "participantIdentifier": "HANDSESS",
                        "name": "Svenska Handelsbanken",
                        "participantType": "FUNDED",
                        "connectingParticipantId": "NDEASESSXXX"
                    },
                    "settlementCounterparty": {
                        "participantIdentifier": "HANDSESS",
                        "name": "Svenska Handelsbanken",
                        "participantType": "FUNDED",
                        "connectingParticipantId": "NDEASESSXXX"
                    },
                    "totalDebit": 10,
                    "totalCredit": 10
                }
              ]
            }
        }
        """
    }

    @Test
    fun `should return 200 on get settlement by cycle and participant id's`() {
        val cycleId = "20201209001"
        val participantId = "HANDSESS"
        val participant = ParticipantReferenceDto(
                participantId, "Svenska Handelsbanken", ParticipantType.FUNDED, null)
        participant.connectingParticipantId = "NDEASESSXXX"
        val instruction = ParticipantInstructionDto.builder()
                .reference("538264950")
                .status(SettlementStatus.PARTIAL)
                .counterparty(participant)
                .settlementCounterparty(participant)
                .totalCredit(BigDecimal.TEN)
                .totalDebit(BigDecimal.TEN)
                .build()
        val details = ParticipantSettlementDetailsDto.builder()
                .cycleId(cycleId)
                .settlementTime(ZonedDateTime.of(2020,12,9,15,58,19, 0, ZoneId.of("UTC")))
                .status(SettlementStatus.PARTIAL)
                .participant(participant)
                .instructions(PageDto(1, listOf(instruction)))
                .build()
        `when`(settlementsFacade.getSettlementDetails(any(), any(), any(), any(), any())).thenReturn(details)
        mockMvc.perform(get("/enquiry/settlements/$cycleId/$participantId")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_DETAILS_RESPONSE))
    }

}
