package com.vocalink.crossproduct.infrastructure.bps.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.vocalink.crossproduct.infrastructure.bps.config.BPSProperties.Detail
import org.springframework.context.annotation.Bean
import java.time.Duration

open class BPSTestConfig {

    @Bean
    open fun testBpsProperties(): BPSProperties {
        val props = BPSProperties()
        props.retryCount = 3
        props.timeoutDuration = Duration.ofMillis(1000)

        props.baseUrls = hashMapOf(Pair("MOCK", "http://localhost:54001"))

        props.paths = hashMapOf<String, Detail>(
                Pair("participants-path", Detail("MOCK", "/participants/P27-SEK/readAll")),
                Pair("participant-path", Detail("MOCK", "/participants/P27-SEK/read")),
                Pair("cycles-path", Detail("MOCK", "/cycles/readAll")),
                Pair("position-details-path", Detail("MOCK", "/positionDetails")),
                Pair("io-participants-path", Detail("MOCK", "/io")),
                Pair("intra-day-position-gross-path", Detail("MOCK", "/settlement/runningDebitCapPositions/readAll")),
                Pair("settlement-position-path", Detail("MOCK", "/settlementPositions/readAll")),
                Pair("io-details-path", Detail("MOCK", "/io-details")),
                Pair("alerts-path", Detail("MOCK", "/alerts")),
                Pair("alert-stats-path", Detail("MOCK", "/alerts/stats")),
                Pair("alert-threshold-path", Detail("MOCK", "/reference/alerts")),
                Pair("message-direction-references-path", Detail("MOCK", "/reference/messages")),
                Pair("alert-stats-path", Detail("MOCK", "/alerts/stats")),
                Pair("file-references-path", Detail("MOCK", "/reference/files")),
                Pair("file-enquiries-path", Detail("MOCK", "/enquiry/files")),
                Pair("batch-enquiries-path", Detail("MOCK", "/enquiry/batches")),
                Pair("single-file-path", Detail("MOCK", "/enquiry/files/read")),
                Pair("single-batch-path", Detail("MOCK", "/enquiry/batches/read")),
                Pair("single-settlement-path", Detail("MOCK", "/enquiry/settlements/read")),
                Pair("instruction-enquiries-path", Detail("MOCK", "/enquiry/instructions")),
                Pair("single-settlement-path", Detail("MOCK", "/enquiry/settlements/read")),
                Pair("settlement-enquiries-path", Detail("MOCK", "/enquiry/settlements"))
        )
        props.retryable = listOf(404)

        return props
    }

    @Bean
    open fun mapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        objectMapper.registerModule(JavaTimeModule())
        return objectMapper
    }
}
