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
        props.schemeCode = "P27-SEK"
        props.currencies = hashMapOf(Pair("P27-SEK", "SEK"))
        props.baseUrls = hashMapOf(Pair("MOCK", "http://localhost:54001"))
        props.eodPeriod = hashMapOf(Pair("start", "16"), Pair("end", "20"))

        props.paths = hashMapOf<String, Detail>(
                Pair("participants-path", Detail("MOCK", "/participants/P27-SEK/readAll")),
                Pair("participant-path", Detail("MOCK", "/participants/P27-SEK/read")),
                Pair("cycles-path", Detail("MOCK", "/cycles/readAll")),
                Pair("day-cycles-path", Detail("MOCK", "/cycles/read")),
                Pair("io-participants-path", Detail("MOCK", "/io")),
                Pair("intra-day-position-gross-path", Detail("MOCK", "/settlement/runningDebitCapPositions/readAll")),
                Pair("settlement-position-path", Detail("MOCK", "/settlement/runningSettlementPositions/readAll")),
                Pair("io-details-path", Detail("MOCK", "/io-details")),
                Pair("alerts-path", Detail("MOCK", "/alerts")),
                Pair("alert-stats-path", Detail("MOCK", "/alerts/stats")),
                Pair("alert-threshold-path", Detail("MOCK", "/reference/alerts")),
                Pair("message-direction-references-path", Detail("MOCK", "/reference/messages")),
                Pair("alert-stats-path", Detail("MOCK", "/alerts/stats")),
                Pair("file-references-path", Detail("MOCK", "/reference/files")),
                Pair("file-enquiries-path", Detail("MOCK", "/enquiries/file/P27-SEK/readAll")),
                Pair("batch-enquiries-path", Detail("MOCK", "/enquiries/batches/P27-SEK/readAll")),
                Pair("single-file-path", Detail("MOCK", "/enquiries/file/P27-SEK/read")),
                Pair("download-file-path", Detail("MOCK", "/enquiries/file/downloadFile/P27-SEK")),
                Pair("single-batch-path", Detail("MOCK", "/enquiries/batches/P27-SEK/read")),
                Pair("single-settlement-path", Detail("MOCK", "/enquiry/settlements/read")),
                Pair("instruction-enquiries-path", Detail("MOCK", "/enquiry/instructions")),
                Pair("single-settlement-path", Detail("MOCK", "/enquiry/settlements/read")),
                Pair("settlement-enquiries-path", Detail("MOCK", "/enquiry/settlements")),
                Pair("transaction-enquiries-path", Detail("MOCK", "/enquiries/transactions/P27-SEK/readAll")),
                Pair("single-transaction-path", Detail("MOCK", "/enquiries/transactions/P27-SEK/read")),
                Pair("single-account-path", Detail("MOCK", "/account/read")),
                Pair("approval-details-path", Detail("MOCK", "/approvals/P27-SEK/read")),
                Pair("approval-create-path", Detail("MOCK", "/approvals/P27-SEK")),
                Pair("broadcasts-path", Detail("MOCK", "/broadcasts/readAll")),
                Pair("broadcasts-create-path", Detail("MOCK", "/broadcasts")),
                Pair("managed-participant-path", Detail("MOCK", "/participants/managed")),
                Pair("approvals-path", Detail("MOCK", "/approvals/P27-SEK/readAll")),
                Pair("participant-configuration-path", Detail("MOCK", "/participants/P27-SEK/configuration")),
                Pair("routing-record-path", Detail("MOCK", "/routing/P27-SEK/records")),
                Pair("reports-path", Detail("MOCK", "/reports")),
                Pair("download-report-path", Detail("MOCK", "/reports/downloadFile/P27-SEK"))
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
