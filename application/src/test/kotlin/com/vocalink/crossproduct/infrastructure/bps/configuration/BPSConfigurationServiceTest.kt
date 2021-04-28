package com.vocalink.crossproduct.infrastructure.bps.configuration

import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfig
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

@BPSTestConfiguration
@Import(BPSConfigurationService::class)
class BPSConfigurationServiceTest @Autowired constructor(
    var bpsConfigurationService: BPSConfigurationService,
    var bpsTestConfig: BPSTestConfig
) {

    @Test
    fun `should return configuration in response`() {
        val scheme = bpsTestConfig.testBpsProperties().schemeCode
        val schemeCurrency = bpsTestConfig.testBpsProperties().currencies.values

        val result = bpsConfigurationService.configuration

        assertThat(result.scheme).isEqualTo(scheme)
        assertThat(result.schemeCurrency).contains(schemeCurrency)
    }
}