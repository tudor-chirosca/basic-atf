package com.vocalink.crossproduct.domain.cycle

import com.vocalink.crossproduct.TestConstants.FIXED_CLOCK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalTime
import java.time.ZonedDateTime
import java.util.stream.Stream

class CycleTest {

    private companion object {
        @JvmStatic
        var clock = FIXED_CLOCK

        @JvmStatic
        fun getData() = Stream.of(
            Arguments.of(null, false),
            Arguments.of(ZonedDateTime.now(clock), false),
            Arguments.of(ZonedDateTime.now(clock).plusDays(1), true),
            Arguments.of(ZonedDateTime.now(clock).with(LocalTime.MAX), false),
            Arguments.of(ZonedDateTime.now(clock).plusDays(1).with(LocalTime.MIN), true)
        )
    }

    @ParameterizedTest(name = "settlement date is \"{0}\", next cycle check should return {1}")
    @MethodSource("getData")
    fun `should check if cycle settlement date is from the next day`(dateTime: ZonedDateTime, isNextCycle: Boolean) {
        val cycle = Cycle.builder().settlementTime(dateTime).build()

        assertThat(cycle.isNextSettlementCycle(clock)).isEqualTo(isNextCycle)
    }
}

