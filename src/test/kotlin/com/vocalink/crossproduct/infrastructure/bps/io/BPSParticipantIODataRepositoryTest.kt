package com.vocalink.crossproduct.infrastructure.bps.io

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.time.LocalDate
import kotlin.test.assertEquals

@BPSTestConfiguration
@Import(BPSParticipantIODataRepository::class)
class BPSParticipantIODataRepositoryTest @Autowired constructor(var client: BPSParticipantIODataRepository, var mockServer: WireMockServer) {

    companion object {
        const val VALID_IO_DETAILS_REQUEST_JSON: String = """
            {
            "schemeParticipantIdentifier" : "NDEASESSXXX"
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
            "files": {
                "submitted": 1,
                "accepted": 1,
                "rejected": "0.00%"
            },
            "batches": [
                {
                    "messageType": "Pacs.008",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Pacs.004",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Pacs.002",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.056",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.029 v3",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.029 v8",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.087",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.027",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Admi.004",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                }
            ],
            "transactions": [
                {
                    "messageType": "Pacs.008",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Pacs.004",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Pacs.002",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.056",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.029 v3",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.029 v8",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.087",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Camt.027",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    }
                },
                {
                    "messageType": "Admi.004",
                    "submitted": 100,
                    "accepted": 100,
                    "amountAccepted": {
                        "amount": 2145.41,
                        "currency": "SEK"
                    },
                    "rejected": "2.31%",
                    "output": 231,
                    "amountOutput": {
                        "amount": 2145.41,
                        "currency": "SEK"
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
                post(urlEqualTo("/schemeIO/participant/P27-SEK/read"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_IO_DETAILS_RESPONSE))
                        .withRequestBody(WireMock.equalToJson(VALID_IO_DETAILS_REQUEST_JSON)))

        val result = client.findByParticipantId("NDEASESSXXX")

        assertEquals(1, result.files.accepted)
        assertEquals(0.0, result.files.rejected)
        assertEquals(1, result.files.submitted)
        assertEquals(2, result.files.output)
        assertEquals(9, result.transactions.size)
        assertEquals(9, result.batches.size)
    }

    @Test
    fun `should pass io data with success`() {
        mockServer.stubFor(
                get(urlEqualTo("/io"))
                        .willReturn(aResponse()
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
