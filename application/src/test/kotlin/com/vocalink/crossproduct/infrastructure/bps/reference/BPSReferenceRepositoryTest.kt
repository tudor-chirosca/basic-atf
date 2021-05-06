package com.vocalink.crossproduct.infrastructure.bps.reference;

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.vocalink.crossproduct.infrastructure.bps.config.BPSTestConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@BPSTestConfiguration
@Import(BPSReferenceRepository::class)
class BPSReferenceRepositoryTest @Autowired constructor(var client: BPSReferenceRepository, var mockServer: WireMockServer) {

    companion object {
        const val VALID_MESSAGE_REFERENCE_RESPONSE: String = """
        [
            {
              "name": "Sending",
              "types": [
                "pacs.004",
                "pacs.008",
                "camt.056",
                "camt.029(v3)",
                "camt.027",
                "camt.087",
                "camt.029(v8)",
                "rocs.001"
              ]
            },
            {
              "name": "Receiving",
              "types": [
                "pacs.004",
                "pacs.002",
                "camt.056",
                "camt.029(v3)",
                "pacs.008",
                "camt.027",
                "camt.087",
                "camt.029(v8)",
                "admi.004",
                "rocs.001",
                "prtp.001-prtp.004",
                "prtp.001FA",
                "prtp.001SO",
                "prtp.005-prtp.006"
            ]
          }
        ]
        """
        const val VALID_FILE_REFERENCES_RESPONSE: String = """
        {
        "validations": [
            {
                "validationLevel": "FILE",
                "reasonCodes": [
                    {
                        "reasonCode": "F00",
                        "description": "Serious structural failure",
                        "active": true
                    }
                ]
            }
            ]
        }
        """
        const val VALID_STATUSES_RESPONSE: String = """ 
    [
        {
            "status": "Accepted"
        },
        {
            "status": "Rejected"
        }
    ]
    """ }

    @Test
    fun `should pass message direction reference success`() {
        mockServer.stubFor(
                post(urlEqualTo("/reference/messages"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_MESSAGE_REFERENCE_RESPONSE)))

        val result = client.findMessageDirectionReferences()

        assertFalse(result.isEmpty())
        assertEquals(2, result.size)

        assertEquals("Sending", result[0].name)
        assertEquals(8, result[0].types.size)

        assertEquals("Receiving", result[1].name)
        assertEquals(14, result[1].types.size)
    }

    @Test
    fun `should pass reason code references with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/reference/reasonCode/P27-SEK/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_FILE_REFERENCES_RESPONSE)))

        val result = client.findReasonCodeReference()

        assertThat(result.validations).isNotEmpty
        assertThat(result.validations[0].validationLevel).isEqualTo(BPSEnquiryType.FILE.name)
        assertThat(result.validations[0].reasonCodes[0].reasonCode).isEqualTo("F00")
        assertThat(result.validations[0].reasonCodes[0].description).isEqualTo("Serious structural failure")
        assertThat(result.validations[0].reasonCodes[0].active).isEqualTo(true)
    }

    @Test
    fun `should pass statuses with success`() {
        mockServer.stubFor(
                post(urlEqualTo("/reference/status/P27-SEK/FILE/readAll"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(VALID_STATUSES_RESPONSE)))

        val result = client.findStatuses("FILE")

        assertThat(result).isNotEmpty
        assertThat(result[0]).isEqualTo("Accepted")
        assertThat(result[1]).isEqualTo("Rejected")
    }
}
