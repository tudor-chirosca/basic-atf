package com.vocalink.crossproduct.infrastructure.bps.report

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.vocalink.crossproduct.domain.participant.Participant
import com.vocalink.crossproduct.domain.participant.ParticipantStatus
import com.vocalink.crossproduct.domain.participant.ParticipantType
import com.vocalink.crossproduct.domain.report.ReportSearchCriteria
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.assertj.core.api.Assertions.assertThat
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
            "sortingOrder": [
                {"sortOrderBy": "reportId", "sortOrder": "ASC"}
            ],
            "reportTypes": [
                {"reportType": "PRE_SETTLEMENT_ADVICE"}
            ],
            "participants": [
                {
                   "schemeCode": "P27-SEK",
                   "schemeParticipantIdentifier": "SWEDSESS",
                   "participantType" : "DIRECT",
                   "status": "ACTIVE",
                   "effectiveFromDate": "2020-03-25T00:00:00Z",
                   "participantName": "Swedbank"
                }
            ],
            "reportId": null,
            "createdFromDate": "2021-01-03T00:00:00Z",
            "createdToDate": null
            }
        """

        var VALID_RESPONSE = """
            {
             "data": [
                      {
                       "reportId": "10000001109",
                       "reportType": "DAILY_SUMMARY_REPORT_SCHEME_OPERATOR_M",
                       "reportName": "Cycle Settlement Report ",
                       "reportDate": "2021-06-03T00:00:00Z",
                       "participantName": "AABA Bank",
                       "partyCode": "AABASESSXXX",
                       "cycleId": "20210529004"
                      }
                      ],
                       "summary": {
                       "offset": 0,
                       "pageSize": 1,
                       "totalCount": 1
                       }
            }
            """
    }

    @Test
    fun `should map full request body and full response and return 200`() {
        val request = ReportSearchCriteria(
            0, 20, listOf("reportId"), listOf("PRE_SETTLEMENT_ADVICE"),
            listOf(Participant.builder()
                .schemeCode("P27-SEK")
                .id("SWEDSESS")
                .participantType(ParticipantType.DIRECT)
                .status(ParticipantStatus.ACTIVE)
                .effectiveFromDate(ZonedDateTime.parse("2020-03-25T00:00:00Z"))
                .name("Swedbank")
                .build()),
            null, ZonedDateTime.parse("2021-01-03T00:00:00Z"), null
        )
        mockServer.stubFor(
            post(urlPathEqualTo("/reports"))
                .withQueryParam("offset", WireMock.equalTo("0"))
                .withQueryParam("pageSize", WireMock.equalTo("20"))
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
            post(urlPathEqualTo("/reports"))
                .willReturn(
                    aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody(VALID_RESPONSE)
                )
                .withRequestBody(equalToJson(VALID_REQUEST))
        )

        val request = ReportSearchCriteria(
            0, 20, listOf("reportId"), listOf("PRE_SETTLEMENT_ADVICE"),
            listOf(Participant.builder()
                .schemeCode("P27-SEK")
                .id("SWEDSESS")
                .participantType(ParticipantType.DIRECT)
                .status(ParticipantStatus.ACTIVE)
                .effectiveFromDate(ZonedDateTime.parse("2020-03-25T00:00:00Z"))
                .name("Swedbank")
                .build()),
            null, ZonedDateTime.parse("2021-01-03T00:00:00Z"), null)

        val result = reportRepository.findPaginated(request)

        assertThat(result).isNotNull
        assertThat(result.items).isEmpty()
        assertThat(result.totalResults).isEqualTo(0)
    }
}