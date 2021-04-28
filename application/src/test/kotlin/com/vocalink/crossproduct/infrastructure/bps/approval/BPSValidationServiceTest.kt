package com.vocalink.crossproduct.infrastructure.bps.approval

import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.validation.BPSValidationService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import java.time.ZoneId
import java.time.ZonedDateTime

@BPSTestConfiguration
@Import(BPSValidationService::class)
class BPSValidationServiceTest @Autowired constructor(var validationService: BPSValidationService) {

    @Test
    fun `should return false and current time in response`() {
        val currentDate = ZonedDateTime.now(ZoneId.of("UTC")).withHour(16).withMinute(1)

        val result = validationService.getApprovalValidation(currentDate)

        assertThat(result.isCanExecute).isEqualTo(false)
        assertThat(result.timestamp).isEqualTo(currentDate)
    }

    @Test
    fun `should return true and current time in response`() {
        val currentDate = ZonedDateTime.now(ZoneId.of("UTC")).withHour(20).withMinute(1)

        val result = validationService.getApprovalValidation(currentDate)

        assertThat(result.isCanExecute).isEqualTo(true)
        assertThat(result.timestamp).isEqualTo(currentDate)
    }
}
