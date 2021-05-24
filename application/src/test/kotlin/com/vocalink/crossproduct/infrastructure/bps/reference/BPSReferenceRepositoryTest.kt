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
                "messageType": "camt.029.001.08",
                "description": "ResponseInquiry",
                "formatName": "camt.029.08",
                "messageDirection": "Input"
            },
            {
                "messageType": "camt.087.001.05",
                "description": "ValueCorrection",
                "formatName": "camt.087",
                "messageDirection": "Output"
            },
            {
                "messageType": "camt.027.001.06",
                "description": "ClaimNonReceipt",
                "formatName": "camt.027",
                "messageDirection": "Input / Output"
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

        assertThat(result.size).isEqualTo(3)

        assertThat(result[0].messageDirection).isEqualTo("sending")
        assertThat(result[1].messageDirection).isEqualTo("receiving")
        assertThat(result[2].messageDirection).isEqualTo("sending / receiving")
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
