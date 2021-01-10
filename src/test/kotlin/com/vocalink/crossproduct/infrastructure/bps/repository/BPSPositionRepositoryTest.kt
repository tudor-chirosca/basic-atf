package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalToJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.position.BPSPositionRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@BPSTestConfiguration
@Import(BPSPositionRepository::class)
class BPSPositionRepositoryTest @Autowired constructor(var client: BPSPositionRepository,
                                                       var mockServer: WireMockServer) {

    companion object {
        const val REQUEST_JSON_POSITION_DETAILS: String = """ 
        {
            "schemeCode" : "P27-SEK",
            "sessionCodes" : null,
            "schemeParticipantIdentifier" : "NDEASESSXXX"
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
            "schemeCode": "P27-SEK",
            "schemeParticipantIdentifier": "NDEASESSXXX",
            "sessionCode": "01",
            "settlementIndicator": "2020081401",
            "customerCreditTransfer": {
                "credit": 6137142,
                "debit": 2416676,
                "netPosition": 3720466
            },
            "paymentReturn": {
                "credit": 4809506,
                "debit": 8433677,
                "netPosition": -3624171
            }
        } 
        """
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should pass position details with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/positionDetails"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_POSITION_DETAILS_RESPONSE))
                        .withRequestBody(equalToJson(REQUEST_JSON_POSITION_DETAILS)))

        val result = client.findByParticipantId("NDEASESSXXX")

        assertEquals(BigDecimal.valueOf(6137142), result[0].customerCreditTransfer.credit)
        assertEquals(BigDecimal.valueOf(8433677), result[0].paymentReturn.debit)
        assertEquals(BigDecimal.valueOf(-3624171), result[0].paymentReturn.netPosition)
        assertEquals("01", result[0].sessionCode)
    }
}
