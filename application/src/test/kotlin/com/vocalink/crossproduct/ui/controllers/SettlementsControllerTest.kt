package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.settlement.InstructionStatus
import com.vocalink.crossproduct.ui.aspects.EventType
import com.vocalink.crossproduct.ui.dto.DefaultDtoConfiguration
import com.vocalink.crossproduct.ui.dto.DtoProperties
import com.vocalink.crossproduct.ui.dto.PageDto
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto
import com.vocalink.crossproduct.ui.dto.reference.ParticipantReferenceDto
import com.vocalink.crossproduct.ui.dto.settlement.LatestSettlementCyclesDto
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantInstructionDto
import com.vocalink.crossproduct.ui.dto.settlement.ParticipantSettlementDetailsDto
import com.vocalink.crossproduct.ui.dto.settlement.SettlementCycleScheduleDto
import com.vocalink.crossproduct.ui.dto.settlement.SettlementScheduleDto
import com.vocalink.crossproduct.ui.facade.api.SettlementsFacade
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class SettlementsControllerTest : ControllerTest() {

    @MockBean
    private lateinit var settlementsFacade: SettlementsFacade

    private companion object {
        const val CONTEXT_HEADER = "context"
        const val CLIENT_TYPE_HEADER = "client-type"

        const val VALID_DETAILS_RESPONSE = """{
            "cycleId": "20201209001",
            "settlementTime": "2020-12-09T15:58:19Z",
            "status": "PARTIALLY_COMPLETE",
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
                    "status": "REJECTED",
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
        const val VALID_SCHEDULE_RESPONSE = """
        {
            "weekdayCycles": [
                {
                    "cycleName": "01",
                    "startTime": "09:00",
                    "cutOffTime": "10:00",
                    "settlementStartTime": "9:45"
                }
            ],
            "weekendCycles": [],
            "updatedAt": "2021-01-11T12:00:00Z"
        }
        """
    }

    @Test
    fun `should return 200 on get settlement by cycle and participant id's`() {
        val cycleId = "20201209001"
        val participantId = "HANDSESS"
        val participant = ParticipantReferenceDto(
                participantId, "Svenska Handelsbanken", ParticipantType.FUNDED, ParticipantStatus.ACTIVE, null)
        participant.connectingParticipantId = "NDEASESSXXX"
        val instruction = ParticipantInstructionDto.builder()
                .reference("538264950")
                .status(InstructionStatus.REJECTED)
                .counterparty(participant)
                .settlementCounterparty(participant)
                .totalCredit(BigDecimal.TEN)
                .totalDebit(BigDecimal.TEN)
                .build()
        val details = ParticipantSettlementDetailsDto.builder()
                .cycleId(cycleId)
                .settlementTime(ZonedDateTime.of(2020,12,9,15,58,19, 0, ZoneId.of("UTC")))
                .status(CycleStatus.PARTIALLY_COMPLETE)
                .participant(participant)
                .instructions(PageDto(1, listOf(instruction)))
                .build()
        `when`(settlementsFacade.getSettlementDetails(any(), any(), any())).thenReturn(details)
        mockMvc.perform(get("/enquiry/settlements/$cycleId/$participantId")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_DETAILS_RESPONSE))

        verify(auditFacade, times(2)).handleEvent(eventCaptor.capture())
        assertAuditEventsSuccess(eventCaptor.allValues[0], eventCaptor.allValues[1], EventType.SETTL_DETAILS)
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

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should return 200 on get settlement schedule` () {
        val schedule = SettlementScheduleDto(
                listOf(SettlementCycleScheduleDto(
                        "01",
                        "09:00",
                        "10:00",
                        "9:45"
                )), emptyList(), ZonedDateTime.of(2021,1,11,12,0,0, 0, ZoneId.of("UTC"))
        )
        `when`(settlementsFacade.getSettlementsSchedule(any(), any())).thenReturn(schedule)
        mockMvc.perform(get("/enquiry/settlements/schedule")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(content().json(VALID_SCHEDULE_RESPONSE))

        verifyNoInteractions(auditFacade)
    }

    @Test
    fun `should fail with 400 when dateFrom is earlier than DAYS_LIMIT from today`() {
        val queryParams = LinkedMultiValueMap(
            mapOf(
                Pair("offset", listOf("0")),
                Pair("limit", listOf("10")),
                Pair("date_from", listOf(
                        ZonedDateTime.now(ZoneId.of("UTC"))
                        .minusDays((DefaultDtoConfiguration.getDefault(DtoProperties.DAYS_LIMIT).toLong()) + 1)
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))))
        )
        mockMvc.perform(
            get("/enquiry/settlements")
                .contentType(UTF8_CONTENT_TYPE)
                .header(CONTEXT_HEADER, TestConstants.CONTEXT)
                .header(CLIENT_TYPE_HEADER, TestConstants.CLIENT_TYPE)
                .queryParams(queryParams))
            .andExpect(status().is4xxClientError)
            .andExpect(content().string(containsString("date_from can not be earlier than DAYS_LIMIT")))

        verifyNoInteractions(auditFacade)
    }
}
