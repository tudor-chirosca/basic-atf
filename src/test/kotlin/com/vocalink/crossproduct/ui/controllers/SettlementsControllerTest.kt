package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.settlement.SettlementStatus
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto
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

        const val VALID_CYCLES_RESPONSE = """{
           "previousCycle": {
               "id": "20210111002",
               "settlementTime": "2021-01-11T12:00:00Z",
               "cutOffTime": "2021-01-11T11:45:00Z",
               "status": "COMPLETED"
           },
           "currentCycle": {
               "id": "20210111003",
               "settlementTime": "2021-01-11T15:00:00Z",
               "cutOffTime": "2021-01-11T14:45:00Z",
               "status": "OPEN"
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

    @Test
    fun `should return 200 on get settlement cycles and return exactly previous and current one` () {
        val latestCycles = LatestSettlementCyclesDto.builder()
                .previousCycle(CycleDto.builder()
                        .id("20210111002")
                        .settlementTime(ZonedDateTime.of(2021,1,11,12,0,0, 0, ZoneId.of("UTC")))
                        .cutOffTime(ZonedDateTime.of(2021,1,11,11,45,0, 0, ZoneId.of("UTC")))
                        .status(CycleStatus.COMPLETED)
                        .build())
                .currentCycle(CycleDto.builder()
                        .id("20210111003")
                        .settlementTime(ZonedDateTime.of(2021,1,11,15,0,0, 0, ZoneId.of("UTC")))
                        .cutOffTime(ZonedDateTime.of(2021,1,11,14,45,0, 0, ZoneId.of("UTC")))
                        .status(CycleStatus.OPEN)
                        .build())
                .build()
        `when`(settlementsFacade.getLatestCycles(any(), any())).thenReturn(latestCycles)
        mockMvc.perform(get("/enquiry/settlements/cycles")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_CYCLES_RESPONSE))
    }

}
