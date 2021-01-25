package com.vocalink.crossproduct.mocks

import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.domain.position.ParticipantPosition
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
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 15, 10, 0, 0, ZoneId.of("UTC")))
                    .id("02")
                    .status(CycleStatus.OPEN)
                    .build(),
            Cycle.builder()
                    .cutOffTime(ZonedDateTime.of(2019, 12, 10, 10, 10, 0, 0, ZoneId.of("UTC")))
                    .settlementTime(ZonedDateTime.of(2019, 12, 10, 12, 10, 0, 0, ZoneId.of("UTC")))
                    .id("01")
                    .status(CycleStatus.COMPLETED)
                    .build())

}
