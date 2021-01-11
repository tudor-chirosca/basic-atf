package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration;
import com.vocalink.crossproduct.infrastructure.bps.position.BPSIntradayPositionGrossRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@BPSTestConfiguration
@Import(BPSIntradayPositionGrossRepository::class)
class BPSIntraDayPositionRepositoryTest @Autowired constructor(
        var intradayPositionRepository: BPSIntradayPositionGrossRepository,
        var mockServer: WireMockServer) {

    companion object {
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
    }

    @Test
    fun `should pass intra-day position with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/intraDayPosition"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_INTRA_DAY_POSITION_RESPONSE))
                        .withRequestBody(equalToJson(REQUEST_JSON_INTRA_DAY_POSITION)))

        val result = intradayPositionRepository.findByIds(listOf("HANDSESS", "ESSESESS"))

        assertTrue(result.isNotEmpty())
        assertEquals("HANDSESS", result[0].participantId)
        assertEquals("ESSESESS", result[1].participantId)
    }
}
