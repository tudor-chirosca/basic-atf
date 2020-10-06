package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.Cycle
import com.vocalink.crossproduct.domain.CycleStatus
import com.vocalink.crossproduct.domain.ParticipantPosition
import com.vocalink.crossproduct.shared.cycle.CPCycle
import com.vocalink.crossproduct.ui.dto.CycleDto
import java.math.BigInteger
import java.time.LocalDateTime

class MockCycles {
    val cyclesDto = listOf(
            CycleDto.builder()
                    .cutOffTime(LocalDateTime.of(2019, 12, 10, 10, 10))
                    .settlementTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                    .id("01")
                    .status(CycleStatus.COMPLETED)
                    .build(),
            CycleDto.builder()
                    .cutOffTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                    .settlementTime(LocalDateTime.of(2019, 12, 10, 15, 10))
                    .id("02")
                    .status(CycleStatus.OPEN)
                    .build())

    val cycles = listOf(
            Cycle.builder()
                    .cutOffTime(LocalDateTime.of(2019, 12, 10, 10, 10))
                    .settlementTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                    .id("01")
                    .status(CycleStatus.COMPLETED)
                    .build(),
            Cycle.builder()
                    .cutOffTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                    .settlementTime(LocalDateTime.of(2019, 12, 10, 15, 10))
                    .id("02")
                    .status(CycleStatus.OPEN)
                    .build())

    val cyclesWithPositions = listOf(
            Cycle.builder()
                    .cutOffTime(LocalDateTime.of(2019, 12, 10, 10, 10))
                    .settlementTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                    .id("01")
                    .status(CycleStatus.COMPLETED)
                    .totalPositions(listOf(
                            MockPositions().getParticipantPosition(false),
                            MockPositions().getParticipantPosition(true),
                            ParticipantPosition.builder()
                                    .participantId("ESSESESS")
                                    .credit(BigInteger.ZERO)
                                    .debit(BigInteger.ONE)
                                    .netPosition(BigInteger.ONE)
                                    .build()
                            ))
                    .build(),
            Cycle.builder()
                    .cutOffTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                    .settlementTime(LocalDateTime.of(2019, 12, 10, 15, 10))
                    .id("02")
                    .status(CycleStatus.OPEN)
                    .totalPositions(listOf(
                            MockPositions().getParticipantPosition(true),
                            MockPositions().getParticipantPosition(false),
                            ParticipantPosition.builder()
                                    .participantId("ESSESESS")
                                    .credit(BigInteger.ZERO)
                                    .debit(BigInteger.ONE)
                                    .netPosition(BigInteger.ONE)
                                    .build()
                    ))
                    .build())

    var cpCycles = listOf(
            CPCycle.builder()
                    .cutOffTime(LocalDateTime.of(2019, 12, 10, 10, 10))
                    .settlementTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                    .id("01")
                    .status("COMPLETED")
                    .build(),
            CPCycle.builder()
                    .cutOffTime(LocalDateTime.of(2019, 12, 10, 12, 10))
                    .settlementTime(LocalDateTime.of(2019, 12, 10, 15, 10))
                    .id("02")
                    .status("OPEN")
                    .build())
}