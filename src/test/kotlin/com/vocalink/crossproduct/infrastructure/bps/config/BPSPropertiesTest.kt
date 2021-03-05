package com.vocalink.crossproduct.infrastructure.bps.config;

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [BPSProperties::class])
@EnableConfigurationProperties
class BPSPropertiesTest @Autowired constructor(private var props: BPSProperties) {

    @Test
    fun `should load all properties`() {

        assertThat(props.timeoutDuration).isNotNull().isPositive()
        assertThat(props.retryCount).isNotNull().isPositive()
        assertThat(props.paths).isNotEmpty
        assertThat(props.paths).isInstanceOf(Map::class.java)
        assertThat(props.baseUrls).isNotEmpty
        assertThat(props.baseUrls).isInstanceOf(Map::class.java)
        assertThat(props.schemeCode).isNotNull()
        assertThat(props.currencies).isInstanceOf(Map::class.java)
    }

}
