package com.vocalink.crossproduct.integrationtests

import com.github.javafaker.Faker
import com.vocalink.crossproduct.domain.*
import com.vocalink.crossproduct.ui.dto.CycleDto
import com.vocalink.crossproduct.ui.dto.SettlementDashboardDto
import com.vocalink.crossproduct.ui.presenter.ClientType
import com.vocalink.crossproduct.ui.presenter.mapper.SelfFundingSettlementDetailsMapper
import com.vocalink.crossproduct.ui.presenter.UIPresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.math.BigInteger
import java.time.LocalDateTime

class GetSettlementTest : AcceptanceTest() {

    @Mock
    private val selfFundingSettlementMapper: SelfFundingSettlementDetailsMapper? = null

    @Test
    fun `should fetch settlement mock data`() {
        val cyclesMock: List<Cycle> = mockCycles()
        val participants: List<Participant> = mockParticipants()
        Mockito.doAnswer { cyclesMock }.`when`(TestApp.cycleRepository).findAll("BPS")
        Mockito.doAnswer { participants }.`when`(TestApp.participantRepository).findAll("BPS")
        Mockito.doAnswer { UIPresenter(selfFundingSettlementMapper) }.`when`(TestApp.presenterFactory).getPresenter(ClientType.UI)

        val expectedCycles = cyclesMock.map {
            CycleDto.builder()
                    .id(it.id)
                    .cutOffTime(it.cutOffTime)
                    .settlementTime(it.settlementTime)
                    .status(it.status)
                    .build()
        }

        val settlementDto = webTestClient
                .get()
                .uri("/settlement")
                .header("context", "BPS")
                .header("client-type", "UI")
                .exchange()
                .expectStatus().isOk
                .expectBody(SettlementDashboardDto::class.java)
                .returnResult()
                .responseBody

        assertThat(expectedCycles).contains(settlementDto?.currentCycle)
        assertThat(expectedCycles).contains(settlementDto?.previousCycle)
    }

    fun mockParticipants(): List<Participant> {
        return listOf(
                Participant
                        .builder()
                        .id("NDEASESS")
                        .bic("NDEASESS")
                        .name("Nordea bank")
                        .status(ParticipantStatus.ACTIVE)
                        .suspendedTime(null)
                        .build(),
                Participant
                        .builder()
                        .id("HANDSESS")
                        .bic("HANDSESS")
                        .name("Svenska Handelsbanken")
                        .status(ParticipantStatus.ACTIVE)
                        .suspendedTime(null)
                        .build()
        )
    }

    fun mockCycles(): List<Cycle> {
        val faker = Faker()
        val creditCurrentCycle = faker.number().digits(7).toLong()
        val debitCurrentCycle = faker.number().digits(7).toLong()

        val creditPreviousCycle = faker.number().digits(7).toLong()
        val debitPreviousCycle = faker.number().digits(7).toLong()

        return listOf(
                Cycle.builder()
                        .cutOffTime(LocalDateTime.now())
                        .settlementTime(LocalDateTime.now())
                        .id("20190212004")
                        .status(CycleStatus.OPEN)
                        .totalPositions(
                                mockParticipants()
                                        .map { participant: Participant ->
                                            ParticipantPosition.builder()
                                                    .participantId(participant.id)
                                                    .credit(BigInteger.valueOf(creditCurrentCycle))
                                                    .debit(BigInteger.valueOf(debitCurrentCycle))
                                                    .netPosition(BigInteger.valueOf(creditCurrentCycle - debitCurrentCycle))
                                                    .build()
                                        }.toList()
                        )
                        .build(),
                Cycle.builder()
                        .cutOffTime(LocalDateTime.now().minusHours(8))
                        .settlementTime(LocalDateTime.now().minusHours(8))
                        .status(CycleStatus.COMPLETED)
                        .id("20190212003")
                        .totalPositions(
                                mockParticipants()
                                        .map { participant: Participant ->
                                            ParticipantPosition.builder()
                                                    .participantId(participant.id)
                                                    .credit(BigInteger.valueOf(creditPreviousCycle))
                                                    .debit(BigInteger.valueOf(debitPreviousCycle))
                                                    .netPosition(BigInteger.valueOf(creditPreviousCycle - debitPreviousCycle))
                                                    .build()
                                        }.toList()
                        ).build()
        )
    }
}
