package com.vocalink.portal

import com.github.javafaker.Faker
import com.vocalink.portal.domain.*
import com.vocalink.portal.ui.dto.CycleDto
import com.vocalink.portal.ui.dto.SettlementDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigInteger
import java.time.LocalDateTime

class GetSettlementTest : AcceptanceTest() {

    @Test
    fun `should fetch settlement mock data`() {
        val cyclesMock: List<Cycle> = mockCycles()
        val participants: List<Participant> = mockParticipants()
        Mockito.doAnswer { cyclesMock }.`when`(TestApp.cycleRepository).fetchCycles()
        Mockito.doAnswer { participants }.`when`(TestApp.participantRepository).fetchParticipants()

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
                .exchange()
                .expectStatus().isOk
                .expectBody(SettlementDto::class.java)
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
                        .cutOffTime(LocalDateTime.now().toString())
                        .settlementTime(LocalDateTime.now().toString())
                        .id("20190212004")
                        .status(CycleStatus.OPEN)
                        .positions(
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
                        .cutOffTime(LocalDateTime.now().minusHours(8).toString())
                        .settlementTime(LocalDateTime.now().minusHours(8).toString())
                        .status(CycleStatus.COMPLETED)
                        .id("20190212003")
                        .positions(
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
