package com.vocalink.crossproduct.ui.presenter.mapper;

import com.vocalink.crossproduct.domain.cycle.Cycle
import com.vocalink.crossproduct.domain.cycle.CycleStatus
import com.vocalink.crossproduct.ui.presenter.mapper.CycleMapper.CYCLE_MAPPER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.Month


class CycleMapperTest {

    @Test
    fun `should map all fields`() {
        val id = "anyId"
        val cutoffTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val settlementTime = LocalDateTime.of(2020, Month.AUGUST, 12, 12, 12)
        val status = CycleStatus.COMPLETED

        val cycle = Cycle.builder()
                .id(id)
                .cutOffTime(cutoffTime)
                .settlementTime(settlementTime)
                .status(status)
                .build()

        val dto = CYCLE_MAPPER.toDto(cycle)

        assertThat(dto.id).isEqualTo(id)
        assertThat(dto.cutOffTime).isEqualTo(cutoffTime)
        assertThat(dto.settlementTime).isEqualTo(settlementTime)
        assertThat(dto.status).isEqualTo(status)
    }
}