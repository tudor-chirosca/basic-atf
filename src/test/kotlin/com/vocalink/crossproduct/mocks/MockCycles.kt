package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.position.ParticipantPosition
import com.vocalink.crossproduct.shared.cycle.CPCycle
import com.vocalink.crossproduct.ui.dto.cycle.CycleDto
import java.math.BigDecimal
import java.time.ZoneId
import java.time.ZonedDateTime

class MockCycles {
    val cyclesDto = listOf(
            CycleDto.builder()
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 10, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .id("01")
                    .status(CycleStatus.COMPLETED)
                    .build(),
            CycleDto.builder()
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 15, 10, 0, 0, ZoneId.of("UTC")))
                    .id("02")
                    .status(CycleStatus.OPEN)
                    .build())

    val cycles = listOf(
            Cycle.builder()
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 10, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .id("01")
                    .status(CycleStatus.COMPLETED)
                    .build(),
            Cycle.builder()
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 15, 10, 0, 0, ZoneId.of("UTC")))
                    .id("02")
                    .status(CycleStatus.OPEN)
                    .build())

    val cyclesWithPositions = listOf(
            Cycle.builder()
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 10, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .id("01")
                    .status(CycleStatus.COMPLETED)
                    .totalPositions(listOf(
                            MockPositions().getParticipantPosition(false),
                            MockPositions().getParticipantPosition(true),
                            ParticipantPosition.builder()
                                    .participantId("ESSESESS")
                                    .credit(BigDecimal.ZERO)
                                    .debit(BigDecimal.ONE)
                                    .netPosition(BigDecimal.ONE)
                                    .build()
                            ))
                    .build(),
            Cycle.builder()
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 15, 10, 0, 0, ZoneId.of("UTC")))
                    .id("02")
                    .status(CycleStatus.OPEN)
                    .totalPositions(listOf(
                            MockPositions().getParticipantPosition(true),
                            MockPositions().getParticipantPosition(false),
                            ParticipantPosition.builder()
                                    .participantId("ESSESESS")
                                    .credit(BigDecimal.ZERO)
                                    .debit(BigDecimal.ONE)
                                    .netPosition(BigDecimal.ONE)
                                    .build()
                    ))
                    .build())

    var cpCycles = listOf(
            CPCycle.builder()
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 10, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .id("01")
                    .status("COMPLETED")
                    .build(),
            CPCycle.builder()
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 15, 10, 0, 0, ZoneId.of("UTC")))
                    .id("02")
                    .status("OPEN")
                    .build())
}