package com.vocalink.crossproduct.ui.controllers

import com.vocalink.crossproduct.TestConfig
import com.vocalink.crossproduct.TestConstants
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.mocks.MockParticipants
import com.vocalink.crossproduct.ui.dto.ParticipantDashboardSettlementDetailsDto
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto
import com.vocalink.crossproduct.ui.dto.participant.ParticipantDto
import com.vocalink.crossproduct.ui.dto.position.ParticipantPositionDto
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsDto
import com.vocalink.crossproduct.ui.dto.position.PositionDetailsTotalsDto
import com.vocalink.crossproduct.ui.dto.position.TotalPositionDto
import com.vocalink.crossproduct.ui.dto.settlement.SettlementDashboardRequest
import com.vocalink.crossproduct.ui.facade.api.SettlementDashboardFacade
import com.vocalink.crossproduct.ui.presenter.ClientType
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(SettlementDashboardController::class)
@ContextConfiguration(classes=[TestConfig::class])
open class SettlementDashboardControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private companion object {
        const val VALID_SETTLEMENT_RESPONSE = """
        {
           "currentCycle":{
              "id":"02",
              "settlementTime":"2019-12-10T15:10:00Z",
              "cutOffTime":"2019-12-10T12:10:00Z",
              "status":"OPEN"
           },
           "previousCycle":{
              "id":"01",
              "settlementTime":"2019-12-10T12:10:00Z",
              "cutOffTime":"2019-12-10T10:10:00Z",
              "status":"COMPLETED"
           },
           "positions":[
              {
                 "participant":{
                    "id":"NDEASESSXXX",
                    "bic":"NDEASESSXXX",
                    "name":"Nordea",
                    "fundingBic":"NA",
                    "status":"ACTIVE",
                    "suspendedTime":null,
                    "participantType":"DIRECT"
                 },
                 "previousPosition":{
                    "credit":10,
                    "debit":10,
                    "netPosition":0
                 },
                 "currentPosition":{
                    "credit":1,
                    "debit":10,
                    "netPosition":9
                 }
              },
              {
                 "participant":{
                    "id":"HANDSESS",
                    "bic":"HANDSESS",
                    "name":"Svenska Handelsbanken",
                    "fundingBic":"NDEASESSXXX",
                    "status":"ACTIVE",
                    "suspendedTime":null,
                    "participantType":"DIRECT"
                 },
                 "previousPosition":{
                    "credit":1,
                    "debit":10,
                    "netPosition":9
                 },
                 "currentPosition":{
                    "credit":10,
                    "debit":10,
                    "netPosition":0
                 }
              }
           ]
        }
        """
    }

    @MockBean
    private lateinit var settlementDashboardFacade: SettlementDashboardFacade

    @Test
    @Throws(Exception::class)
    fun `should get settlement for scheme operator`() {
        val position1 = ParticipantPositionDto.builder()
                .credit(BigDecimal.TEN)
                .debit(BigDecimal.TEN)
                .netPosition(BigDecimal.ZERO)
                .build()
        val position2 = ParticipantPositionDto.builder()
                .credit(BigDecimal.ONE)
                .debit(BigDecimal.TEN)
                .netPosition(BigDecimal.valueOf(9))
                .build()
        val previousCycle = CycleDto.builder()
                .cutOffTime(ZonedDateTime.of(2019, 12, 10, 10, 10, 0, 0, ZoneId.of("UTC")))
                .settlementTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                .id("01")
                .status(CycleStatus.COMPLETED)
                .build()
        val currentCycle = CycleDto.builder()
                .cutOffTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                .settlementTime(ZonedDateTime.of(2019, 12, 10, 15, 10, 0, 0, ZoneId.of("UTC")))
                .id("02")
                .status(CycleStatus.OPEN)
                .build()
        val participant1 = ParticipantDto.builder()
                .id("NDEASESSXXX")
                .bic("NDEASESSXXX")
                .fundingBic("NA")
                .name("Nordea")
                .status(ParticipantStatus.ACTIVE)
                .participantType(ParticipantType.DIRECT)
                .build()
        val participant2 = ParticipantDto.builder()
                .id("HANDSESS")
                .bic("HANDSESS")
                .fundingBic("NDEASESSXXX")
                .name("Svenska Handelsbanken")
                .status(ParticipantStatus.ACTIVE)
                .participantType(ParticipantType.DIRECT)
                .build()
        val totalPositions = listOf(
                TotalPositionDto.builder()
                        .currentPosition(position2)
                        .previousPosition(position1)
                        .participant(participant1)
                        .build(),
                TotalPositionDto.builder()
                        .currentPosition(position1)
                        .previousPosition(position2)
                        .participant(participant2)
                        .build()
        )
        val dto = SettlementDashboardDto.builder()
            .previousCycle(previousCycle)
            .currentCycle(currentCycle)
            .positions(totalPositions)
            .build()
        `when`(settlementDashboardFacade.getParticipantSettlement(eq(TestConstants.CONTEXT), eq(ClientType.UI), any()))
            .thenReturn(dto)
        mockMvc.perform(get("/settlement")
            .header("context", TestConstants.CONTEXT)
            .header("client-type", TestConstants.CLIENT_TYPE))
            .andExpect(status().isOk)
            .andExpect(content().json(VALID_SETTLEMENT_RESPONSE))
    }

    @Test
    @Throws(Exception::class)
    fun `should get self funding settlement details for given participant id`() {
        val participantId = "NDEASESSXXX"
        val currentPositionTotals = PositionDetailsTotalsDto(BigDecimal.TEN, BigDecimal.TEN)
        currentPositionTotals.totalNetPosition = BigDecimal.ZERO
        val previousPositionTotals = PositionDetailsTotalsDto(BigDecimal.TEN, BigDecimal.TEN)
        previousPositionTotals.totalNetPosition =  BigDecimal.ZERO
        val previousCycle = CycleDto.builder()
                .cutOffTime(ZonedDateTime.of(2019, 12, 10, 10, 10, 0, 0, ZoneId.of("UTC")))
                .settlementTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                .id("01")
                .status(CycleStatus.COMPLETED)
                .build()
        val currentCycle = CycleDto.builder()
                .cutOffTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                .settlementTime(ZonedDateTime.of(2019, 12, 10, 15, 10, 0, 0, ZoneId.of("UTC")))
                .id("02")
                .status(CycleStatus.OPEN)
                .build()
        val positionDetails = PositionDetailsDto(
                ParticipantPositionDto.builder()
                        .credit(BigDecimal.ONE)
                        .debit(BigDecimal.TEN)
                        .netPosition(BigDecimal.valueOf(9))
                        .build(),
                ParticipantPositionDto.builder()
                        .credit(BigDecimal.TEN)
                        .debit(BigDecimal.TEN)
                        .netPosition(BigDecimal.ZERO)
                        .build()
        )
        val dto = ParticipantDashboardSettlementDetailsDto(
                MockParticipants().getParticipantDto(false),
                currentCycle,
                previousCycle,
                positionDetails,
                positionDetails,
                previousPositionTotals,
                currentPositionTotals,
                null, null
        )
        `when`(settlementDashboardFacade.getParticipantSettlementDetails(TestConstants.CONTEXT, ClientType.UI, participantId))
                .thenReturn(dto)
        mockMvc.perform(get("/settlement/$participantId")
                .header("context", TestConstants.CONTEXT)
                .header("client-type", TestConstants.CLIENT_TYPE))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.previousCycle.id").value("01"))
                .andExpect(jsonPath("$.previousCycle.status").value("COMPLETED"))
                .andExpect(jsonPath("$.currentCycle.id").value("02"))
                .andExpect(jsonPath("$.currentCycle.status").value("OPEN"))
                .andExpect(jsonPath("$.participant.id").value("NDEASESSXXX"))
                .andExpect(jsonPath("$.participant.bic").value("NDEASESSXXX"))
                .andExpect(jsonPath("$.participant.name").value("Nordea"))
                .andExpect(jsonPath("$.participant.status").value("ACTIVE"))
                .andExpect(jsonPath("$.participant.participantType").value("DIRECT"))
                .andExpect(jsonPath("$.previousPosition.customerCreditTransfer.credit").value("1"))
                .andExpect(jsonPath("$.previousPosition.customerCreditTransfer.debit").value("10"))
                .andExpect(jsonPath("$.previousPosition.customerCreditTransfer.netPosition").value("9"))
                .andExpect(jsonPath("$.currentPosition.customerCreditTransfer.credit").value("1"))
                .andExpect(jsonPath("$.currentPosition.customerCreditTransfer.debit").value("10"))
                .andExpect(jsonPath("$.currentPosition.customerCreditTransfer.netPosition").value("9"))
                .andExpect(jsonPath("$.previousPosition.paymentReturn.credit").value("10"))
                .andExpect(jsonPath("$.previousPosition.paymentReturn.debit").value("10"))
                .andExpect(jsonPath("$.previousPosition.paymentReturn.netPosition").value("0"))
                .andExpect(jsonPath("$.currentPosition.paymentReturn.credit").value("10"))
                .andExpect(jsonPath("$.currentPosition.paymentReturn.debit").value("10"))
                .andExpect(jsonPath("$.currentPosition.paymentReturn.netPosition").value("0"))
                .andExpect(jsonPath("$.previousPositionTotals.totalCredit").value("10"))
                .andExpect(jsonPath("$.previousPositionTotals.totalDebit").value("10"))
                .andExpect(jsonPath("$.previousPositionTotals.totalNetPosition").value("0"))
                .andExpect(jsonPath("$.currentPositionTotals.totalCredit").value("10"))
                .andExpect(jsonPath("$.currentPositionTotals.totalDebit").value("10"))
                .andExpect(jsonPath("$.currentPositionTotals.totalNetPosition").value("0"))
    }
}
