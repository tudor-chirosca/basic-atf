package com.vocalink.crossproduct.infrastructure.bps.cycle

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

@BPSTestConfiguration
@Import(BPSCycleRepository::class)
class BPSCycleRepositoryTest @Autowired constructor(var cycleRepository: BPSCycleRepository,
                                                    var mockServer: WireMockServer) {

    companion object {
        const val VALID_CYCLE_REQUEST_JSON: String = """{
                    "schemeCode": "P27-SEK"
                }"""

        const val VALID_DAY_CYCLE_REQUEST_JSON: String = """{
                    "schemeCode": "P27-SEK",
                    "settlementDate" : "2020-11-05"
                }"""

        const val VALID_SETTLEMENT_REQUEST_JSON: String = """{
                    "schemeCode": "P27-SEK",
                    "currency": null,
                    "participantIds": null,
                    "numberOfCycles": null
                }"""

        const val VALID_DAY_CYCLE_RESPONSE: String = """{
                    "cycleCode": "C1",
                    "sessionCode": "01",
                    "sessionInstanceId": "20210218001",
                    "status": "COMPLETED",
                    "createdDate": "2021-02-18T14:00:00Z",
                    "updatedDate": "2021-02-18T15:00:00Z"
                }"""


        const val VALID_CYCLE_RESPONSE: String = """{
            "currency": "SEK",
            "schemeCode": "P27-SEK",
            "cycles": [
                    {
                    "cycleId": "20201209001",
                    "status": "OPEN",
                    "settlementTime": "2020-12-09T14:58:19Z",
                    "fileSubmissionCutOffTime": "2020-12-09T15:58:19Z",
                    "isNextDayCycle": false,
                    "settlementConfirmationTime": "2020-12-09T15:58:19Z"
                     },
                     {
                    "cycleId": "20201209002",
                    "status": "OPEN",
                    "settlementTime": "2020-12-09T14:58:19Z",
                    "fileSubmissionCutOffTime": "2020-11-05T15:58:19Z",
                    "isNextDayCycle": false,
                    "settlementConfirmationTime": "2020-12-09T15:58:19Z"
                     }
             ]
             }"""

        const val VALID_SETTLEMENT_RESPONSE: String =
                """{
                        "schemeId": "P27-SEK",
                        "mlSettlementPositions": [
                            {
                                "settlementDate": "2020-12-09",
                                "participantId": "HANDSESS",
                                "cycleId": "001",
                                "currency": "SEK",
                                "paymentSent": {
                                    "count": 2,
                                    "amount": {
                                        "amount": 2145.41,
                                        "currency": "SEK"
                                    }
                                },
                                "paymentReceived": {
                                    "count": 2,
                                    "amount": {
                                        "amount": 2145.41,
                                        "currency": "SEK"
                                    }
                                },
                                "returnSent": {
                                    "count": 2,
                                    "amount": {
                                        "amount": 2145.41,
                                        "currency": "SEK"
                                    }
                                },
                                "returnReceived": {
                                    "count": 2,
                                    "amount": {
                                        "amount": 2145.41,
                                        "currency": "SEK"
                                    }
                                },
                                "netPositionAmount": {
                                    "amount": 2145.41,
                                    "currency": "SEK"
                                }
                            }
                        ]
                }"""
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should pass with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/settlement/runningSettlementPositions/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(VALID_SETTLEMENT_RESPONSE))
                        .withRequestBody(equalToJson(VALID_SETTLEMENT_REQUEST_JSON)))

        mockServer.stubFor(
                post(urlEqualTo("/cycles/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(VALID_CYCLE_RESPONSE))
                        .withRequestBody(equalToJson(VALID_CYCLE_REQUEST_JSON)))

        val findAll = cycleRepository.findAll()

        assertThat(findAll).isNotEmpty
        assertThat(findAll[0].totalPositions).isNotEmpty
        assertThat(findAll[0].id).isEqualTo("20201209001")
    }

    @Test
    fun `should return cycles by date with success`() {
        val date = LocalDate.parse("2020-11-05", DateTimeFormatter.ISO_DATE)

        mockServer.stubFor(
                post(urlEqualTo("/cycles/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(VALID_DAY_CYCLE_RESPONSE))
                        .withRequestBody(equalToJson(VALID_DAY_CYCLE_REQUEST_JSON)))

        val result = cycleRepository.findByDate(date)

        assertThat(result).isNotEmpty
    }

    @Test
    fun `should return cycle by ids` () {
        val cycleIds  = listOf("20201209001", "20201209002")

        mockServer.stubFor(
                post(urlEqualTo("/cycles/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(VALID_CYCLE_RESPONSE))
                        .withRequestBody(equalToJson(VALID_CYCLE_REQUEST_JSON)))

        val result = cycleRepository.findByIds(cycleIds)

        assertThat(result).isNotEmpty
        assertThat(result[0].id).isEqualTo(cycleIds[0])
    }
}
