package com.vocalink.crossproduct.infrastructure.bps.report

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.domain.report.ReportSearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.exception.InfrastructureException
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import java.time.ZonedDateTime

@BPSTestConfiguration
@Import(BPSReportRepository::class)
class BPSReportRepositoryTest @Autowired constructor(
    var reportRepository: BPSReportRepository,
    var mockServer: WireMockServer
) {

    companion object {
        var VALID_REQUEST = """
            {
            "offset": 0,
            "limit": 20,
            "sort": ["reportId"],
            "reportTypes": null,
            "participants": null,
            "id": null,
            "date_from": "2021-01-03T00:00:00Z",
            "date_to": null
            }
        """

        var VALID_RESPONSE = """
            {
            "totalResults": 1,
            "items": [
                {
                    "reportId": "10000000004",
                    "reportType": "CYCLE_SETTLEMENT_REPORT",
                    "createdAt": "2021-01-24T00:00:00Z",
                    "cycleId": "20201231001",
                    "participantIdentifier": "SWEDSESS",
                    "participantName": "Swedbank"
                }
            ]
        }
        """
    }

    @Test
    fun `should map full request body and full response and return 200`() {
        val request = ReportSearchCriteria(
            0, 20, listOf("reportId"), null, null,
            null, ZonedDateTime.parse("2021-01-03T00:00:00Z"), null
        )
        mockServer.stubFor(
            post(urlEqualTo("/reports"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(VALID_RESPONSE))
                .withRequestBody(equalToJson(VALID_REQUEST, true, false)))

        reportRepository.findPaginated(request)
    }

    @Test
    fun `should fail if 404 returned and throw ClientException`() {
        mockServer.stubFor(
            post(urlEqualTo("/reports"))
                .willReturn(
                    aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(VALID_RESPONSE)
                )
                .withRequestBody(equalToJson(VALID_REQUEST))
        )

        assertThatExceptionOfType(InfrastructureException::class.java).isThrownBy {
            reportRepository.findPaginated(
                ReportSearchCriteria(
                    0, 20, listOf("reportId"), null, null,
                    null, ZonedDateTime.parse("2021-01-03T00:00:00Z"), null)
            )
        }.withMessageContaining("Timeout!")
    }
}