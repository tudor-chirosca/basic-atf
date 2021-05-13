package com.vocalink.crossproduct.infrastructure.bps.cycle

import com.vocalink.crossproduct.TestConstants
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BPSSettlementPositionTest {

    private var rawCycleId = "005"

    private companion object {
        @JvmStatic
        var clock = TestConstants.FIXED_CLOCK!!
        var formattedDate = LocalDate.now(clock)
            .format(DateTimeFormatter.ofPattern(BPSSettlementPosition.CYCLE_ID_DATE_FORMAT))!!
    }

    @Test
    fun `should return cycleId based on settlementDate`() {
        val position = BPSSettlementPosition.builder()
            .rawCycleId(rawCycleId)
            .settlementDate(LocalDate.now(clock))
            .build()

        assertThat(position.cycleId).isEqualTo("$formattedDate$rawCycleId")
    }

    @Test
    fun `should return cycleId if settlementDate is null`() {
        val position = BPSSettlementPosition.builder()
            .rawCycleId(rawCycleId)
            .settlementDate(null)
            .build()

        assertThat(position.cycleId).isEqualTo(rawCycleId)
    }

    @Test
    fun `should return null as cycleId if all field are null`() {
        val position = BPSSettlementPosition.builder()
            .rawCycleId(null)
            .settlementDate(null)
            .build()

        assertThat(position.cycleId).isNull()
    }
}