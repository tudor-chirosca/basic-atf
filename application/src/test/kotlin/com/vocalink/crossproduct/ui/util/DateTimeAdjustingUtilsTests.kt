package com.vocalink.crossproduct.ui.util

import com.vocalink.crossproduct.ui.util.DateTimeAdjustingUtils.adjustWithZoneId
import java.time.ZoneId
import java.time.ZonedDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DateTimeAdjustingUtilsTests {

    @Test
    fun `should convert to cet from utc using value from properties`() {
        val pseudoUTC = ZonedDateTime.of(2021, 6, 20, 10, 10, 10, 0, ZoneId.of("UTC"))
        val CET = adjustWithZoneId(pseudoUTC, "CET")
        assertThat(CET).isEqualTo(pseudoUTC.plusHours(2))
    }

    @Test
    fun `should return null value if no value date`() {
        val UTC = adjustWithZoneId(null, "CET")
        assertThat(UTC).isNull()
    }
}