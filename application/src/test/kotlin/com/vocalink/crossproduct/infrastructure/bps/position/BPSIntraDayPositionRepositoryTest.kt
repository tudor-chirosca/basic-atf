package com.vocalink.crossproduct.infrastructure.bps.position

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSIntradayPositionGrossRepository::class)
class BPSIntraDayPositionRepositoryTest @Autowired constructor(
        var intradayPositionRepository: BPSIntradayPositionGrossRepository,
        var mockServer: WireMockServer) {

    companion object {
        const val REQUEST_JSON_INTRA_DAY_POSITION: String = """ 
        {
            "schemeCode" : "P27-SEK",
            "participantId" : "NDEASESSXXX"
        } 
        """

        const val VALID_INTRA_DAY_POSITION_RESPONSE: String = """
        [    
            {
                "schemeId": "P27-SEK",
                "debitParticipantId": "HANDSESS",
                "settlementDate": "2021-01-13",
                "debitCapAmount": {
                    "amount": 10000.00,
                    "currency": "SEK"
                },
                "debitPositionAmount": {
                    "amount": 9877.53,
                    "currency": "SEK"
                }
            },
            {
                "schemeId": "P27-SEK",
                "debitParticipantId": "SWCTSES1",
                "settlementDate": "2021-01-13",
                "debitCapAmount": {
                    "amount": 10003.00,
                    "currency": "SEK"
                },
                "debitPositionAmount": {
                    "amount": 9880.53,
                    "currency": "SEK"
                }
            },
            {
                "schemeId": "P27-SEK",
                "debitParticipantId": "ELLFSESS",
                "settlementDate": "2021-01-13",
                "debitCapAmount": {
                    "amount": 10004.00,
                    "currency": "SEK"
                },
                "debitPositionAmount": {
                    "amount": 9881.53,
                    "currency": "SEK"
                }
            }
        ]    
        """
    }

    @Test
    fun `should pass intra-day position with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/settlement/runningDebitCapPositions/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_INTRA_DAY_POSITION_RESPONSE))
                        .withRequestBody(equalToJson(REQUEST_JSON_INTRA_DAY_POSITION)))

        val result = intradayPositionRepository.findById("NDEASESSXXX")

        assertThat(result).isNotEmpty
        assertThat(result[0].debitParticipantId).isEqualTo("HANDSESS")
    }

    @Test
    fun `should pass intra-day position with success on 404 NOT_FOUND response`() {
        mockServer.stubFor(
                post(urlEqualTo("/settlement/runningDebitCapPositions/readAll"))
                        .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_INTRA_DAY_POSITION_RESPONSE))
                        .withRequestBody(equalToJson(REQUEST_JSON_INTRA_DAY_POSITION)))

        val result = intradayPositionRepository.findById("NDEASESSXXX")

        assertThat(result).isEmpty()
    }

    @Test
    fun `should pass intra-day position with success on 204 NO_CONTENT response`() {
        mockServer.stubFor(
                post(urlEqualTo("/settlement/runningDebitCapPositions/readAll"))
                        .willReturn(aResponse()
                                .withStatus(204)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_INTRA_DAY_POSITION_RESPONSE))
                        .withRequestBody(equalToJson(REQUEST_JSON_INTRA_DAY_POSITION)))

        val result = intradayPositionRepository.findById("NDEASESSXXX")

        assertThat(result).isEmpty()
    }
}
