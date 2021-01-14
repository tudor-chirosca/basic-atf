package com.vocalink.crossproduct.infrastructure.bps.repository

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import com.vocalink.crossproduct.infrastructure.bps.io.BPSParticipantIODataRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

@BPSTestConfiguration
@Import(BPSParticipantIODataRepository::class)
class BPSParticipantIODataRepositoryTest @Autowired constructor(var client: BPSParticipantIODataRepository, var mockServer: WireMockServer) {

    companion object {
        const val VALID_IO_DETAILS_REQUEST_JSON: String = """
            {
                "schemeCode" : "P27-SEK",
                "participantId" : "NDEASESSXXX",
                "date" : "2020-10-20"
            }
        """
        const val VALID_IO_RESPONSE: String = """ 
        {
            "participantId": "NDEASESSXXX",
            "files": {
                "submitted": 506,
                "rejected": 0.53
            },
            "batches": {
                "submitted": 1702,
                "rejected": 3.65
            },
            "transactions": {
                "submitted": 63451,
                "rejected": 3.49
            }
        } 
        """

        const val VALID_IO_DETAILS_RESPONSE: String = """
        {
           "schemeParticipantIdentifier": "NDEASESSXXX",
           "files": {
             "submitted": 1,
             "accepted": 1,
             "output": 2,
             "rejected": 1.22
           },
           "batches": [
             {
               "name": "Customer Credit Transfer",
               "code": "Pacs.008",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22
               }
             },
             {
               "name": "Payment Return",
               "code": "Pacs.004",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22
               }
             },
             {
               "name": "Payment Reversal",
               "code": "Pacs.002",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22
               }
             },
             {
               "name": "Cancellation Request",
               "code": "Camt.056",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22
               }
             },
             {
               "name": "Resolution of Investigation",
               "code": "Camt.029 v3",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22
               }
             },
             {
               "name": "Resolution of Investigation",
               "code": "Camt.029 v8",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22
               }
             },
             {
               "name": "Request to Modify Payment",
               "code": "Camt.087",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22
               }
             },
             {
               "name": "Claim Non-Receipt",
               "code": "Camt.027",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22
               }
             },
             {
               "name": "Admin Output",
               "code": "Admi004",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22
               }
             }
           ],
           "transactions": [
             {
               "name": "Customer Credit Transfer",
               "code": "Pacs.008",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22,
                 "amountAccepted": 1,
                 "amountOutput": 1
               }
             },
             {
               "name": "Payment Return",
               "code": "Pacs.004",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22,
                 "amountAccepted": 1,
                 "amountOutput": 1
               }
             },
             {
               "name": "Payment Reversal",
               "code": "Pacs.002",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22,
                 "amountAccepted": 1,
                 "amountOutput": 1
               }
             },
             {
               "name": "Cancellation Request",
               "code": "Camt.056",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22,
                 "amountAccepted": 1,
                 "amountOutput": 1
               }
             },
             {
               "name": "Resolution of Investigation",
               "code": "Camt.029 v3",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22,
                 "amountAccepted": 1,
                 "amountOutput": 1
               }
             },
             {
               "name": "Resolution of Investigation",
               "code": "Camt.029 v8",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22,
                 "amountAccepted": 1,
                 "amountOutput": 1
               }
             },
             {
               "name": "Request to Modify Payment",
               "code": "Camt.087",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22,
                 "amountAccepted": 1,
                 "amountOutput": 1
               }
             },
             {
               "name": "Claim Non-Receipt",
               "code": "Camt.027",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22,
                 "amountAccepted": 1,
                 "amountOutput": 1
               }
             },
             {
               "name": "Admin Output",
               "code": "Admi004",
               "data": {
                 "submitted": 1,
                 "accepted": 1,
                 "output": 2,
                 "rejected": 1.22,
                 "amountAccepted": 1,
                 "amountOutput": 1
               }
             }
           ]
         }
        """
    }

    @AfterEach
    fun cleanUp() {
        mockServer.resetAll()
    }

    @Test
    fun `should pass io details with success`() {
        mockServer.stubFor(
                WireMock.post(WireMock.urlEqualTo("/io-details"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_IO_DETAILS_RESPONSE))
                        .withRequestBody(WireMock.equalToJson(VALID_IO_DETAILS_REQUEST_JSON)))

        val string = "2020-10-20"
        val date = LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
        val result = client.findIODetailsFor("NDEASESSXXX", date)

        assertEquals("NDEASESSXXX", result[0].schemeParticipantIdentifier)
        assertEquals(1, result[0].files.accepted)
        assertEquals(1.22, result[0].files.rejected)
        assertEquals(1, result[0].files.submitted)
        assertEquals(2, result[0].files.output)
        assertEquals(9, result[0].transactions.size)
        assertEquals(9, result[0].batches.size)
    }

    @Test
    fun `should pass io data with success`() {
        mockServer.stubFor(
                WireMock.get(WireMock.urlEqualTo("/io"))
                        .willReturn(WireMock.aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_IO_RESPONSE)))

        val result = client.findByTimestamp(LocalDate.now())

        assertEquals("NDEASESSXXX", result[0].participantId)
        assertEquals(3.65, result[0].batches.rejected)
        assertEquals(1702, result[0].batches.submitted)
        assertEquals(0.53, result[0].files.rejected)
        assertEquals(506, result[0].files.submitted)
        assertEquals(3.49, result[0].transactions.rejected)
        assertEquals(63451, result[0].transactions.submitted)
    }
}
