package com.vocalink.crossproduct.infrastructure.bps.config;

import com.vocalink.crossproduct.infrastructure.config.WireMockInitializer
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(classes = [BPSTestConfig::class, BPSRetryWebClientConfig::class, BPSClientConfig::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = [WireMockInitializer::class])
@DirtiesContext
annotation class BPSTestConfiguration
