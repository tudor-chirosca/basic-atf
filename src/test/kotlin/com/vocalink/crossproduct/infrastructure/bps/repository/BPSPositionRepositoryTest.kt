package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.position.BPSPositionRepository
import java.math.BigDecimal
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@BPSTestConfiguration
@Import(BPSPositionRepository::class)
class BPSPositionRepositoryTest @Autowired constructor(var client: BPSPositionRepository,
                                                       var mockServer: WireMockServer) {

    companion object {
        const val REQUEST_JSON_POSITION_DETAILS: String = """ 
        {
            "schemeCode" : "P27-SEK",
            "currency" : null,
            "participantIds" : ["NDEASESSXXX"],
            "numberOfCycles" : null
        } 
        """
        const val REQUEST_JSON_INTRA_DAY_POSITION: String = """ 
        {
            "schemeCode" : "P27-SEK",
            "schemeParticipantIdentifiers" :["HANDSESS", "ESSESESS"]
        } 
        """

        const val VALID_INTRA_DAY_POSITION_RESPONSE: String = """
        [    
            {
                "participantId": "HANDSESS",
                "debitCap": 10000.00,
                "debitPosition": 9877.53
            },
            {
                "participantId": "ESSESESS",
                "debitCap": 10001.00,
                "debitPosition": 9878.53
            }
        ]    
        """

        const val VALID_POSITION_DETAILS_RESPONSE: String = """ 
        {
            "schemeId": "P27-SEK",
            "mlSettlementPositions":
            [
                {
                    "settlementDate": "2021-01-15",
                    "participantId": "HANDSESS",
                    "cycleId": "20210122001",
                    "currency": "SEK",
                    "paymentSent": {
                        "count": 2,
                        "amount": {
                            "amount": 100.11,
                            "currency": "SEK"
                        }
                    },
                    "paymentReceived": {
                        "count": 2,
                        "amount": {
                            "amount": 100.11,
                            "currency": "SEK"
                        }
                    },
                    "returnSent": {
                        "count": 2,
                        "amount": {
                            "amount": 200.11,
                            "currency": "SEK"
                        }
                    },
                    "returnReceived": {
                        "count": 2,
                        "amount": {
                            "amount": 200.11,
                            "currency": "SEK"
                        }
                    },
                    "netPositionAmount": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                }
            ]
        }
        }"""
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should pass position details with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/settlement/runningSettlementPositions/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_POSITION_DETAILS_RESPONSE))
                        .withRequestBody(equalToJson(REQUEST_JSON_POSITION_DETAILS)))

        val result = client.findByParticipantId("NDEASESSXXX")[0]

        assertThat(result.settlementDate).isEqualTo(LocalDate.of(2021, 1, 15))
        assertThat(result.participantId).isEqualTo("HANDSESS")
        assertThat(result.cycleId).isEqualTo("20210122001")
        assertThat(result.currency).isEqualTo("SEK")

        assertThat(result.paymentSent.count).isEqualTo(2)
        assertThat(result.paymentSent.amount.amount).isEqualTo(BigDecimal.valueOf(100.11))
        assertThat(result.paymentSent.amount.currency).isEqualTo("SEK")

        assertThat(result.paymentReceived.count).isEqualTo(2)
        assertThat(result.paymentReceived.amount.amount).isEqualTo(BigDecimal.valueOf(100.11))
        assertThat(result.paymentReceived.amount.currency).isEqualTo("SEK")

        assertThat(result.returnSent.count).isEqualTo(2)
        assertThat(result.returnSent.amount.amount).isEqualTo(BigDecimal.valueOf(200.11))
        assertThat(result.returnSent.amount.currency).isEqualTo("SEK")

        assertThat(result.returnReceived.count).isEqualTo(2)
        assertThat(result.returnReceived.amount.amount).isEqualTo(BigDecimal.valueOf(200.11))
        assertThat(result.returnReceived.amount.currency).isEqualTo("SEK")

        assertThat(result.netPositionAmount.amount).isEqualTo(BigDecimal.valueOf(2145.41))
        assertThat(result.netPositionAmount.currency).isEqualTo("SEK")
    }
}
