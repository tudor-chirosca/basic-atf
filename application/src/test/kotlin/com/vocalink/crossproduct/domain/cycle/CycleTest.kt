package com.vocalink.crossproduct.domain.cycle

import com.vocalink.crossproduct.TestConstants.FIXED_CLOCK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
            Arguments.of(ZonedDateTime.now(clock), null, true),
            Arguments.of(ZonedDateTime.now(clock), ZonedDateTime.now(clock), false),
            Arguments.of(ZonedDateTime.now(clock), ZonedDateTime.now(clock).plusDays(1), false),
            Arguments.of(ZonedDateTime.now(clock), ZonedDateTime.now(clock).with(LocalTime.MAX), false),
            Arguments.of(ZonedDateTime.now(clock), ZonedDateTime.now(clock).plusDays(1).with(LocalTime.MIN), false),
            Arguments.of(ZonedDateTime.now(clock), ZonedDateTime.now(clock).minusDays(1), true),
            Arguments.of(ZonedDateTime.now(clock), ZonedDateTime.now(clock).minusDays(1).with(LocalTime.MAX), true)
        )
    }

    @ParameterizedTest(name = "current: \"{0}\", previous: \"{1}\", check should return: {2}")
    @MethodSource("getData")
    fun `should check if cycle settlement date is from the next day`(
        currentDateTime: ZonedDateTime,
        previousDateTime: ZonedDateTime?, isNextDayCycle: Boolean
    ) {
        val currentCycle = Cycle.builder().settlementTime(currentDateTime).build()
        val previousCycle = Cycle.builder().settlementTime(previousDateTime).build()

        assertThat(currentCycle.isPreviousDayCycle(previousCycle)).isEqualTo(isNextDayCycle)
    }

    @Test
    fun `should be isEmpty if Cycle has no id`() {
        val cycle = Cycle.builder().build()

        assertThat(cycle.isEmpty).isTrue()
    }

    @Test
    fun `should not isEmpty if cycle id is not null`() {
        val cycle = Cycle.builder()
            .id("005")
            .build()

        assertThat(cycle.isEmpty).isFalse()
    }

    @Test
    fun `should be in EOD SOD period`() {
        val currentCycle = Cycle.builder()
            .id("005")
            .status(CycleStatus.NOT_STARTED)
            .build()
        val previousCycle = Cycle.builder()
            .id("004")
            .status(CycleStatus.CLOSED)
            .build()

        assertThat(currentCycle.isInEodSodPeriod(previousCycle)).isTrue()
    }

    @Test
    fun `should be in EOD SOD period if previous cycle does not exist`() {
        val currentCycle = Cycle.builder()
            .id("005")
            .status(CycleStatus.NOT_STARTED)
            .build()

        assertThat(currentCycle.isInEodSodPeriod(null)).isTrue()
    }

    @Test
    fun `should throw exception if previous cycle is still open when EOD SOD`() {
        val currentCycle = Cycle.builder()
            .id("005")
            .status(CycleStatus.NOT_STARTED)
            .build()
        val previousCycle = Cycle.builder()
            .id("004")
            .status(CycleStatus.OPEN)
            .build()

        assertThrows<IllegalStateException> { currentCycle.isInEodSodPeriod(previousCycle) }
    }
}

